package com.yilian.mall.shoppingcard.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.jd.adapter.JdShoppingCarAdapter;
import com.yilian.mall.jd.enums.JdShoppingCartType;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.adapter.CardGoodsListAdapter;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.NumberFormat;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.jd.JDGoodsListEntity;
import com.yilian.networkingmodule.entity.jd.JdShoppingCarEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 购物卡 京东购物清单
 *
 * @author Created by Zg on 2018/11/18.
 */

public class CardJdShoppingListActivity extends BaseFragmentActivity {
    public static final int MAX_GOODS_COUNTS = 200;
    public PopupWindow mPop;
    public JdShoppingCarEntity mEntity;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;
    private PopupWindow mDelPop;
    private SwipeRefreshLayout jdSwipeRefreshLayout;
    private RecyclerView jdCarRecyclerView;
    private JdShoppingCarAdapter carAdapter;
    private CheckBox checkBoxAll;
    private TextView tvTotalPrice;
    private TextView tvGoToPay;
    private TextView tvDeleted;
    /**
     * 删除商品Id
     * 购物车ID 多个以逗号分隔
     */
    private String cardIndex;
    /**
     * 是否是编辑状态
     */
    private boolean isEditStatus = false;
    private int curGoodsNum;
    private EditText tvCurGoodsNum;
    private TextView tvGoodsKinds;
    private String curGoodsSku;
    /**
     * 编辑状态下的list
     */
    private List<JdShoppingCarEntity.ListBean> editBeanList = new ArrayList<>();
    /**
     * 结算状态下的list
     */
    private List<JdShoppingCarEntity.ListBean> accountBeanList = new ArrayList<>();
    private RecyclerView jdRecommendRecyclerView;
    private int page = 0;
    private CardGoodsListAdapter jdHomeListAdapter;
    private VaryViewUtils varyViewUtils;
    private List<JDGoodsAbstractInfoEntity> goodsList = new ArrayList<>();
    private LinearLayout llCarShopping;
    private View sub;
    private View add;
    private View tvConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_jd_shopping_list);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        hidePop();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public boolean hidePop() {
        if (mPop != null && mPop.isShowing()) {
            mPop.dismiss();
            return true;
        }
        if (mDelPop != null && mDelPop.isShowing()) {
            mDelPop.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 设置结算状态
     */
    public void setAccountBeanList(String skuString) {
        isEditStatus = false;
        accountBeanList.clear();
        editBeanList.clear();
        if (!TextUtils.isEmpty(skuString)) {
            String[] sku = skuString.split(",");
            for (String goodsSku : sku) {
                JdShoppingCarEntity.ListBean bean = new JdShoppingCarEntity.ListBean(Parcel.obtain());
                bean.setChecked(true);
                bean.goodsSku = goodsSku;
                accountBeanList.add(bean);
            }
            setCurEditStatus();
            jdCarRecyclerView.smoothScrollToPosition(0);
            calculatePrice();
            checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
        }
    }

    private void setCurEditStatus() {
        if (isEditStatus) {
            tvRight.setText("完成");
            tvGoToPay.setVisibility(View.GONE);
            tvTotalPrice.setVisibility(View.GONE);
            tvDeleted.setVisibility(View.VISIBLE);
            carAdapter.updateSkuList(editBeanList);
        } else {
            tvRight.setText("编辑");
            tvGoToPay.setVisibility(View.VISIBLE);
            tvDeleted.setVisibility(View.GONE);
            tvTotalPrice.setVisibility(View.VISIBLE);
            carAdapter.updateSkuList(accountBeanList);
        }
        carAdapter.updateNewData(carAdapter.getData());
    }

    /**
     * 计算价格
     */
    private void calculatePrice() {
        String totalMoney = "0.00";
        int goodsCounts = 0;
        for (JdShoppingCarEntity.ListBean bean : carAdapter.getData()) {
            if (bean.isChecked) {
                String moneyString = MyBigDecimal.mul(bean.goodsInfo.jdPrice, bean.goodsCount);
                totalMoney = MyBigDecimal.add(moneyString, totalMoney);
                goodsCounts += Integer.parseInt(bean.goodsCount);
            }
        }
        tvGoToPay.setText("去结算（" + goodsCounts + "）");
        tvTotalPrice.setText("合计：" + MoneyUtil.add¥Front(totalMoney));
    }

    public void setBeanList() {
        if (isEditStatus) {
            editBeanList.clear();
        } else {
            accountBeanList.clear();
        }
        for (JdShoppingCarEntity.ListBean listBean : carAdapter.getData()) {
            JdShoppingCarEntity.ListBean bean = new JdShoppingCarEntity.ListBean(Parcel.obtain());
            bean.setChecked(listBean.isChecked);
            bean.goodsSku = listBean.goodsSku;
            if (isEditStatus) {
                editBeanList.add(bean);
            } else {
                accountBeanList.add(bean);
            }
        }
    }

    /**
     * 获取下一页数据
     */
    private void getNextGoodsData() {
        page++;
        getJdRecommendGoodsData();
    }

    private void initView() {
        llCarShopping = findViewById(R.id.ll_shopping_car);
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setJdVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                getShoppingCarData();
            }
        });
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);

        jdRecommendRecyclerView = (RecyclerView) findViewById(R.id.jd_recommend_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);

        jdRecommendRecyclerView.setLayoutManager(gridLayoutManager);
        jdHomeListAdapter = new CardGoodsListAdapter();
        jdRecommendRecyclerView.setAdapter(jdHomeListAdapter);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(context, 8, R.color.color_bg, true);
        jdRecommendRecyclerView.addItemDecoration(decor);
        View headView = View.inflate(context, R.layout.jd_header_shopping_car_empty, null);
        jdHomeListAdapter.addHeaderView(headView);


        tvDeleted = findViewById(R.id.tv_delete);
        tvGoToPay = findViewById(R.id.tv_go_to_pay);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        checkBoxAll = findViewById(R.id.check_box_goods_all);
        jdCarRecyclerView = findViewById(R.id.jd_car_recycler_view);
        carAdapter = new JdShoppingCarAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        jdCarRecyclerView.setLayoutManager(manager);
        jdCarRecyclerView.setAdapter(carAdapter);
        jdSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        jdSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.red));
    }

    /**
     * 获取购物车列表
     */
    @SuppressWarnings("unchecked")
    public void getShoppingCarData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getJdShoppingCarList("jd_card_cart/card_cart_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JdShoppingCarEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        jdSwipeRefreshLayout.setRefreshing(false);
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                        tvRight.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(JdShoppingCarEntity entity) {
                        mEntity = entity;
                        if (entity.getList().size() > 0) {
                            llCarShopping.setVisibility(View.VISIBLE);
                            varyViewUtils.showDataView();
                            setJdShoppingCarData(entity);
                            jdSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            llCarShopping.setVisibility(View.GONE);
                            getFirstGoodsData();
                        }
                        setTvRightStatus(mEntity);
                        //通知购物车变化
                        EventBus.getDefault().post(new JdShoppingCarEntity());
                    }
                });
        addSubscription(subscription);
    }

    private void setJdShoppingCarData(JdShoppingCarEntity entity) {
        if (isEditStatus) {
            carAdapter.updateSkuList(editBeanList);
        } else {
            carAdapter.updateSkuList(accountBeanList);
        }
        carAdapter.updateNewData(entity.getList());
        checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
        calculatePrice();
    }

    public void setTvRightStatus(JdShoppingCarEntity entity) {
        if (entity == null || entity.getList().size() <= 0) {
            tvRight.setVisibility(View.GONE);
        } else {
            tvRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取您推荐数据
     */
    private void getFirstGoodsData() {
        page = 0;
        getJdRecommendGoodsData();
    }

    /**
     * 京东推荐
     */
    @SuppressWarnings("unchecked")
    private void getJdRecommendGoodsData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .getJDGoodsListData("jd_goods/jd_recomand_list", page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JDGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        jdSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        jdSwipeRefreshLayout.setRefreshing(false);
                        if (page == Constants.PAGE_INDEX) {
                            varyViewUtils.showErrorView();
                        } else if (page > Constants.PAGE_INDEX) {
                            page--;
                        }
                        showToast(e.getMessage());
                        jdHomeListAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(JDGoodsListEntity o) {
                        setGoodsData(o);
                    }
                });
        addSubscription(subscription);

    }

    private void setGoodsData(JDGoodsListEntity entity) {
        if (page <= 0) {
            if (entity.getData().size() == 0) {
                jdHomeListAdapter.loadMoreEnd();
                varyViewUtils.showEmptyView();
            } else {
                varyViewUtils.showDataView();
                goodsList.clear();
                goodsList.addAll(entity.getData());
                jdHomeListAdapter.setNewData(goodsList);
                if (entity.getData().size() < Constants.PAGE_COUNT) {
                    jdHomeListAdapter.loadMoreEnd();
                } else {
                    jdHomeListAdapter.loadMoreComplete();
                }
            }

        } else {
            goodsList.addAll(entity.getData());
            jdHomeListAdapter.addData(entity.getData());
            if (entity.getData().size() < Constants.PAGE_COUNT) {
                jdHomeListAdapter.loadMoreEnd();
            } else {
                jdHomeListAdapter.loadMoreComplete();
            }

        }
    }

    private void initData() {
        tvTitle.setText("购物清单");
        tvRight.setText("编辑");
        String skuString = getIntent().getStringExtra("sku_string");
        if (!TextUtils.isEmpty(skuString)) {
            setAccountBeanList(skuString);
        }
        getShoppingCarData();
    }

    private void initListener() {

        jdHomeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDGoodsAbstractInfoEntity entity = (JDGoodsAbstractInfoEntity) jdHomeListAdapter.getItem(position);
                JumpCardActivityUtils.toGoodsDetail(context, entity.skuId);
            }
        });
        jdHomeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, jdRecommendRecyclerView);
        carAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JdShoppingCarEntity.ListBean listBean = (JdShoppingCarEntity.ListBean) adapter.getItem(position);
                JumpCardActivityUtils.toGoodsDetail(context, listBean.goodsSku);
            }
        });
        RxUtil.clicks(ivBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
               finish();
            }
        });
        RxUtil.clicks(tvDeleted, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                int selectedNum = carAdapter.getCheckedList().size();
                if (selectedNum > 0) {
                    showDelPop(selectedNum + "");
                } else {
                    showToast("您还没有选择商品哦");
                }
            }
        });
        RxUtil.clicks(tvGoToPay, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!isLogin()) {
                    startActivity(new Intent(context, LeFenPhoneLoginActivity.class));
                } else {
                    int selectedNum = carAdapter.getCheckedList().size();
                    if (selectedNum > 0) {
                        List<JdShoppingCarEntity.ListBean> listBeans = carAdapter.getCheckedList();
                        ArrayList<JDCommitOrderEntity> jdCommitOrderEntities = new ArrayList<>();
                        for (JdShoppingCarEntity.ListBean listBean : listBeans) {
                            jdCommitOrderEntities.add(
                                    new JDCommitOrderEntity(listBean.goodsInfo.name, listBean.goodsInfo.sku, listBean.goodsInfo.imagePath,
                                            NumberFormat.convertToInt(listBean.goodsCount, 0), listBean.goodsInfo.price,
                                            listBean.goodsInfo.jdPrice, listBean.goodsInfo.returnBean, listBean.cartIndex));
                        }
                        JumpJdActivityUtils.toJDCommitOrderActivity(context, null, jdCommitOrderEntities, JdShoppingCartType.FROM_SHOPPING_CART,JumpToOtherPageUtil.JD_GOODS_TYPE_CARD);
                    } else {
                        showToast("您还没有选择商品哦");
                    }

                }
            }
        });
        checkBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carAdapter.updateAllCheckBox(checkBoxAll.isChecked());
                setBeanList();
                calculatePrice();
            }
        });
        carAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                JdShoppingCarEntity.ListBean listBean = carAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.chk_shopping_cart_goods_list:
                        boolean isChecked = listBean.isChecked;
                        listBean.setChecked(!isChecked);
                        checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
                        setBeanList();
                        calculatePrice();
                        break;
                    case R.id.ll_shopping_num:
                        showUpdatePop(listBean);
                        break;
                    default:
                        break;
                }

            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditStatus) {
                    editBeanList.clear();
                } else {
                    setBeanList();
                }
                isEditStatus = !isEditStatus;
                setCurEditStatus();
                jdCarRecyclerView.smoothScrollToPosition(0);
                calculatePrice();
                checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
            }
        });
        jdSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShoppingCarData();
            }
        });
    }

    private void showUpdatePop(JdShoppingCarEntity.ListBean listBean) {
        curGoodsNum = Integer.parseInt(listBean.goodsCount);
        curGoodsSku = listBean.goodsSku;
        if (mPop == null) {
            View contentView = View.inflate(context, R.layout.layout_pop_jd_shopping_goods_num, null);
            mPop = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            tvCurGoodsNum = contentView.findViewById(R.id.tv_goods_num);
            sub = contentView.findViewById(R.id.jd_iv_sub);
            add = contentView.findViewById(R.id.jd_iv_add);
            tvConfirm = contentView.findViewById(R.id.tv_confirm);
            mPop.setBackgroundDrawable(new ColorDrawable());
            mPop.setOutsideTouchable(true);
            mPop.setFocusable(true);
            RxUtil.clicks(contentView.findViewById(R.id.fl_root_view), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                }
            });
            RxUtil.clicks(contentView.findViewById(R.id.tv_cancel), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                }
            });
            RxUtil.clicks(tvConfirm, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    updateJdShoppingCarGoodsCounts();
                }
            });
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (curGoodsNum > 1) {
                        curGoodsNum--;
                        tvCurGoodsNum.setText(String.valueOf(curGoodsNum) + "");
                        tvCurGoodsNum.setSelection(String.valueOf(curGoodsNum).length());
                    }
                }
            });
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (curGoodsNum < MAX_GOODS_COUNTS) {
                        curGoodsNum++;
                        tvCurGoodsNum.setText(String.valueOf(curGoodsNum) + "");
                        tvCurGoodsNum.setSelection(String.valueOf(curGoodsNum).length());
                    }
                }
            });
            tvCurGoodsNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String str = s.toString().trim();
                    if (TextUtils.isEmpty(str)) {
                        tvCurGoodsNum.setText("1");
                        tvCurGoodsNum.setSelection(1);
                    } else {
                        int num = Integer.parseInt(str);
                        if (num < 1) {
                            tvCurGoodsNum.setText(1 + "");
                            tvCurGoodsNum.setSelection(1);
                        }
                        if (num > MAX_GOODS_COUNTS) {
                            tvCurGoodsNum.setText(MAX_GOODS_COUNTS + "");
                            tvCurGoodsNum.setSelection(String.valueOf(MAX_GOODS_COUNTS).length());
                        }
                    }
                }
            });
        }
        tvCurGoodsNum.setText(curGoodsNum + "");
        tvCurGoodsNum.setSelection(String.valueOf(curGoodsNum).length());
        mPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void showDelPop(String strNum) {
        if (mDelPop == null) {
            View contentView = View.inflate(context, R.layout.layout_pop_jd_shopping_car_del, null);
            mDelPop = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mDelPop.setContentView(contentView);
            tvGoodsKinds = contentView.findViewById(R.id.tv_goods_kind);
            View tvConfirm = contentView.findViewById(R.id.tv_confirm);
            mDelPop.setBackgroundDrawable(new ColorDrawable());
            mDelPop.setOutsideTouchable(true);
            RxUtil.clicks(contentView.findViewById(R.id.fl_root_view), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                }
            });
            RxUtil.clicks(contentView.findViewById(R.id.tv_cancel), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                }
            });
            RxUtil.clicks(tvConfirm, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                    delGoodsFromJdShoppingCar();
                }
            });
        }
        tvGoodsKinds.setText("确认要删除这" + strNum + "种商品吗？");
        mDelPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 清单删除
     */
    @SuppressWarnings("unchecked")
    private void delGoodsFromJdShoppingCar() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .delGoodsFromJdShoppingCar("jd_card_cart/card_cart_del", getDelGoodsIds())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                        hidePop();
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        showToast(o.msg);
                        hidePop();
                        getShoppingCarData();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取删除商品id
     *
     * @return
     */
    private String getDelGoodsIds() {
        cardIndex = "";
        List<JdShoppingCarEntity.ListBean> listBeans = carAdapter.getCheckedList();
        for (int i = 0; i < listBeans.size(); i++) {
            if (listBeans.get(i).isChecked) {
                cardIndex += listBeans.get(i).cartIndex + ",";
            }
        }
        if (cardIndex.length() > 0) {
            cardIndex = cardIndex.substring(0, cardIndex.lastIndexOf(","));
        }
        return cardIndex;

    }
    /**
     * 通知刷新购物车数据
     *{@link com.yilian.mall.jd.activity.JdHomePageActivity#onNewIntent(Intent intent)}
     * {@link com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity#addJdGoodsToShoppingCar()}
     * @param entity
     */
    @Subscribe
    public void updateShoppingCarData(JdShoppingCarEntity entity) {
        getShoppingCarData();
    }

    /**
     * 京东购物车更新
     */
    @SuppressWarnings("unchecked")
    private void updateJdShoppingCarGoodsCounts() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context)
                .updateJdShoppingCarGoodsCounts("jd_card_cart/card_cart_upd", tvCurGoodsNum.getText().toString(), curGoodsSku)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        varyViewUtils.showErrorView();
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        showToast(o.msg);
                        getShoppingCarData();
                        hidePop();
                    }
                });
        addSubscription(subscription);

    }
}

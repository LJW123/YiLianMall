package com.yilian.mall.suning.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.suning.activity.SnCommitOrderActivity;
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.adapter.SnHomeListAdapter;
import com.yilian.mall.suning.adapter.SnShoppingCarAdapter;
import com.yilian.mall.suning.module.SuNingCommitOrderGoodsModule;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.LoadingShopCarActivity;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.suning.SnGoodsAbstractInfoEntity;
import com.yilian.networkingmodule.entity.suning.SnGoodsListEntity;
import com.yilian.networkingmodule.entity.suning.SnShoppingCarEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 苏宁购物车
 *
 * @author Created by zhaiyaohua on 2018/6/27.
 */

public class SnShoppingCarFragment extends JPBaseFragment {
    public static final int MAX_GOODS_COUNTS = 200;
    public PopupWindow mPop;
    public SnShoppingCarEntity mEntity;
    private PopupWindow mDelPop;
    private SwipeRefreshLayout jdSwipeRefreshLayout;
    private RecyclerView snCarRecyclerView;
    private SnShoppingCarAdapter carAdapter;
    private SnHomePageActivity homePageActivity;
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
    private String curSkuId;
    /**
     * 编辑状态下的list
     */
    private List<SnShoppingCarEntity.ListBean> editBeanList = new ArrayList<>();
    /**
     * 结算状态下的list
     */
    private List<SnShoppingCarEntity.ListBean> accountBeanList = new ArrayList<>();
    private RecyclerView snRecommendRecyclerView;
    private int page = 0;
    private SnHomeListAdapter snHomeListAdapter;
    private VaryViewUtils varyViewUtils;
    private List<SnGoodsAbstractInfoEntity> goodsList = new ArrayList<>();
    //购物撤展示 购物车空页面
    private LinearLayout llCarShopping, llShoppingCarEmpty;
    //推荐标题栏
    private LinearLayout llRecommend;
    //购物车按钮栏
    private LinearLayout llCartSettlement;

    private View sub;
    private View add;
    private View tvConfirm;
    private boolean isHidden = true;
    /**
     * 购物车为空时  去逛逛
     */
    private TextView tvGoShopping;

    /**
     * 设置结算状态
     */
    public void setAccountBeanList(String skuString) {
        isEditStatus = false;
        accountBeanList.clear();
        editBeanList.clear();
        if (!TextUtils.isEmpty(skuString)) {
            String[] sku = skuString.split(",");
            for (String skuId : sku) {
                SnShoppingCarEntity.ListBean bean = new SnShoppingCarEntity.ListBean(Parcel.obtain());
                bean.setChecked(true);
                bean.skuId = skuId;
                accountBeanList.add(bean);
            }
            setCurEditStatus();
            snCarRecyclerView.smoothScrollToPosition(0);
            calculatePrice();
            checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
        }
    }

    private void setCurEditStatus() {
        if (isEditStatus) {
            homePageActivity.snTvRight.setText("完成");
            tvGoToPay.setVisibility(View.GONE);
            tvTotalPrice.setVisibility(View.GONE);
            tvDeleted.setVisibility(View.VISIBLE);
            carAdapter.updateSkuList(editBeanList);
        } else {
            homePageActivity.snTvRight.setText("编辑");
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
        for (SnShoppingCarEntity.ListBean bean : carAdapter.getData()) {
            if (bean.isChecked) {
                String moneyString = MyBigDecimal.mul(bean.goodsInfo.getSnPrice(), bean.num);
                totalMoney = MyBigDecimal.add(moneyString, totalMoney);
                goodsCounts += Integer.parseInt(bean.num);
            }
        }
        tvGoToPay.setText(String.format(Locale.getDefault(),"去结算(%d)", goodsCounts));
        tvTotalPrice.setText(Html.fromHtml("<font color='#333333'>合计：</font>" + MoneyUtil.add¥Front(totalMoney)));
    }

    public void setBeanList() {
        if (isEditStatus) {
            editBeanList.clear();
        } else {
            accountBeanList.clear();
        }
        for (SnShoppingCarEntity.ListBean listBean : carAdapter.getData()) {
            SnShoppingCarEntity.ListBean bean = new SnShoppingCarEntity.ListBean(Parcel.obtain());
            bean.setChecked(listBean.isChecked);
            bean.skuId = listBean.skuId;
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
        getSnRecommendGoodsData();
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);

        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setSnVaryViewHelper(R.id.vary_content, view, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                getShoppingCarData();
                getFirstGoodsData();
            }
        });

        snRecommendRecyclerView = (RecyclerView) view.findViewById(R.id.sn_recommend_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);

        snRecommendRecyclerView.setLayoutManager(gridLayoutManager);
        snHomeListAdapter = new SnHomeListAdapter(goodsList);
        snRecommendRecyclerView.setAdapter(snHomeListAdapter);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg, true);
        snRecommendRecyclerView.addItemDecoration(decor);

        //设置头部
        View headView = View.inflate(mContext, R.layout.sn_header_shopping_car, null);
        llCarShopping = headView.findViewById(R.id.ll_shopping_car);
        snCarRecyclerView = headView.findViewById(R.id.sn_car_recycler_view);
        carAdapter = new SnShoppingCarAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        snCarRecyclerView.setLayoutManager(manager);
        snCarRecyclerView.setNestedScrollingEnabled(false);
        snCarRecyclerView.setAdapter(carAdapter);
        llShoppingCarEmpty = headView.findViewById(R.id.ll_shopping_car_empty);
        tvGoShopping = headView.findViewById(R.id.tv_go_shopping);
        llRecommend = headView.findViewById(R.id.ll_recommend);
        snHomeListAdapter.addHeaderView(headView);

        llCartSettlement = view.findViewById(R.id.ll_cart_settlement);
        tvDeleted = view.findViewById(R.id.tv_delete);
        tvGoToPay = view.findViewById(R.id.tv_go_to_pay);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        checkBoxAll = view.findViewById(R.id.check_box_goods_all);

        jdSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        jdSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.red));
        homePageActivity = (SnHomePageActivity) getActivity();
        homePageActivity.setSnTvRightMark(true);
        startMyDialog();
        getShoppingCarData();
        getFirstGoodsData();
    }

    private void initListener() {
        snHomeListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnGoodsAbstractInfoEntity entity = (SnGoodsAbstractInfoEntity) snHomeListAdapter.getItem(position);
                JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, entity.skuId);
            }
        });
        snHomeListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextGoodsData();
            }
        }, snRecommendRecyclerView);
        carAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnShoppingCarEntity.ListBean listBean = (SnShoppingCarEntity.ListBean) adapter.getItem(position);
                JumpSnActivityUtils.toSnGoodsDetailActivity(mContext, listBean.goodsInfo.getSkuId());
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
        RxUtil.clicks(tvGoShopping, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_HOME, null);
            }
        });

        RxUtil.clicks(tvGoToPay, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

                if (!isLogin()) {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                } else {
                    // 整理商品 跳转提交订单
                    ArrayList<SuNingCommitOrderGoodsModule> list = new ArrayList<>();
                    for (SnShoppingCarEntity.ListBean bean : carAdapter.getCheckedList()) {
                        list.add(
                                new SuNingCommitOrderGoodsModule(
                                        bean.skuId,
                                        Integer.valueOf(bean.num),
                                        bean.goodsInfo.getName(),
                                        bean.goodsInfo.getSnPrice(),
                                        bean.goodsInfo.getReturnBean(),
                                        bean.goodsInfo.getImage(), bean.cartIndex));
                    }
                    JumpSnActivityUtils.toSnCommitOrderActivity(mContext, null, list, SnCommitOrderActivity.FromeType.SN_SHOPPING_CART);
                }
            }
        });
        checkBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carAdapter.updateAllCheckBox(checkBoxAll.isChecked(), isEditStatus);
                setBeanList();
                calculatePrice();
            }
        });
        carAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SnShoppingCarEntity.ListBean listBean = carAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.chk_shopping_cart_goods_list:
                        //上下架状态 1在售 0下架
                        if (listBean.goodsInfo.getState() == 0 && !isEditStatus) {
                            //下架且处于非编辑状态
                            showToast("该商品已下架");
                            CheckBox checkBox = view.findViewById(R.id.chk_shopping_cart_goods_list);
                            if (checkBox != null) {
                                checkBox.setChecked(false);
                            }
                        } else {
                            boolean isChecked = listBean.isChecked;
                            listBean.setChecked(!isChecked);
                            checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
                            setBeanList();
                            calculatePrice();
                        }
                        break;
                    case R.id.tv_shopping_num:
                        showUpdatePop(listBean);
                        break;
                    case R.id.num_sub:
                        if (Integer.valueOf(listBean.num) > 1) {
                            updateSnShoppingCarGoodsCounts(String.valueOf(Integer.valueOf(listBean.num) - 1), listBean.skuId);
                        } else {
                            showToast("受不了了，宝贝不能再减少了哦~");
                        }
                        break;
                    case R.id.num_add:
                        updateSnShoppingCarGoodsCounts(String.valueOf(Integer.valueOf(listBean.num) + 1), listBean.skuId);
                        break;
                    default:
                        break;
                }

            }
        });
        homePageActivity.snTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditStatus) {
                    editBeanList.clear();
                } else {
                    setBeanList();
                }
                isEditStatus = !isEditStatus;
                setCurEditStatus();
                snCarRecyclerView.smoothScrollToPosition(0);
                calculatePrice();
                checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
            }
        });
        jdSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getShoppingCarData();
                getFirstGoodsData();
            }
        });
    }

    private void showUpdatePop(SnShoppingCarEntity.ListBean listBean) {
        curGoodsNum = Integer.parseInt(listBean.num);
        curSkuId = listBean.skuId;
        if (mPop == null) {
            View contentView = View.inflate(mContext, R.layout.layout_pop_jd_shopping_goods_num, null);
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
                    updateSnShoppingCarGoodsCounts(tvCurGoodsNum.getText().toString(), curSkuId);
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
        mPop.showAtLocation(homePageActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void showDelPop(String strNum) {
        if (mDelPop == null) {
            View contentView = View.inflate(mContext, R.layout.layout_pop_jd_shopping_car_del, null);
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
        mDelPop.showAtLocation(homePageActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @SuppressWarnings("unchecked")
    private void delGoodsFromJdShoppingCar() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .delGoodsFromSnShoppingCar("suning_cart/suning_cart_del", getDelGoodsIds())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
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
        List<SnShoppingCarEntity.ListBean> listBeans = carAdapter.getCheckedList();
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

    private void showDialog() {
        Logger.i("2017年12月13日 14:09:01-走了show-cart");
        startActivity(new Intent(mContext, LoadingShopCarActivity.class));
    }

    private void stopDialog() {
        Logger.i("2017年12月13日 14:09:01-走了stop-cart");
        AppManager.getInstance().killActivity(LoadingShopCarActivity.class);
    }

    /**
     * 苏宁购物车更新
     */
    @SuppressWarnings("unchecked")
    private void updateSnShoppingCarGoodsCounts(String num, String skuId) {
        showDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .updateSnShoppingCarGoodsCounts("suning_cart/suning_cart_upd", num, skuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopDialog();
                    }

                    @Override
                    public void onNext(HttpResultBean o) {
                        getShoppingCarData();
                        hidePop();
                    }
                });
        addSubscription(subscription);

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

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.activity_sn_shopping_car, null);
        view.setClickable(true);
        initView(view);
        initListener();
        return view;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        isHidden = hidden;
        if (!hidden) {
            setTvRightStatus(mEntity);
        } else {
            homePageActivity.snTvRight.setVisibility(View.GONE);
        }
        super.onHiddenChanged(hidden);
    }

    public void setTvRightStatus(SnShoppingCarEntity entity) {
        if (entity == null || entity.getList().size() <= 0) {
            if (homePageActivity != null) {
                homePageActivity.snTvRight.setVisibility(View.GONE);
            }

        } else {
            if (homePageActivity != null) {
                homePageActivity.snTvRight.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 通知刷新购物车数据
     * {@link SnHomePageActivity#onNewIntent(Intent intent)}
     * {@link com.yilian.mall.jd.activity.goodsdetail.JDGoodsDetailActivity#addJdGoodsToShoppingCar()}
     *
     * @param entity
     */
    @Subscribe
    public void updateShoppingCarData(SnShoppingCarEntity entity) {
        getShoppingCarData();
    }

    /**
     * 获取购物车列表
     */
    @SuppressWarnings("unchecked")
    public void getShoppingCarData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnShoppingCarList("suning_cart/suning_cart_list")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SnShoppingCarEntity>() {
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
                        homePageActivity.snTvRight.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(SnShoppingCarEntity entity) {
                        mEntity = entity;
                        if (entity.getList().size() > 0) {
                            llCarShopping.setVisibility(View.VISIBLE);
                            llShoppingCarEmpty.setVisibility(View.GONE);
                            llRecommend.setBackgroundColor(Color.parseColor("#f7f7f7"));
                            llCartSettlement.setVisibility(View.VISIBLE);

                            varyViewUtils.showDataView();
                            setSnShoppingCarData(entity);
                            jdSwipeRefreshLayout.setRefreshing(false);

                        } else {
                            llCarShopping.setVisibility(View.GONE);
                            llShoppingCarEmpty.setVisibility(View.VISIBLE);
                            llRecommend.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                            llCartSettlement.setVisibility(View.GONE);
                        }
                        if (!isHidden) {
                            setTvRightStatus(mEntity);
                        } else {
                            homePageActivity.snTvRight.setVisibility(View.GONE);
                        }

                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取您推荐数据
     */
    private void getFirstGoodsData() {
        page = 0;
        getSnRecommendGoodsData();
    }

    private void setSnShoppingCarData(SnShoppingCarEntity entity) {
        if (isEditStatus) {
            carAdapter.updateSkuList(editBeanList);
        } else {
            carAdapter.updateSkuList(accountBeanList);
        }
        //处理数据  下架商品置于列表尾部
        List<SnShoppingCarEntity.ListBean> cartList = new ArrayList<>();
        List<SnShoppingCarEntity.ListBean> soldOutList = new ArrayList<>();
        for (SnShoppingCarEntity.ListBean bean : entity.getList()) {
            //上下架状态 1在售 0下架
            if (bean.goodsInfo.getState() == 1) {
                cartList.add(bean);
            } else {
                soldOutList.add((bean));
            }
        }
        cartList.addAll(soldOutList);
        carAdapter.updateNewData(cartList);
        checkBoxAll.setChecked(carAdapter.getAllCheckedStatus());
        calculatePrice();
    }

    /**
     * 苏宁推荐
     */
    @SuppressWarnings("unchecked")
    private void getSnRecommendGoodsData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getSnGoodsListData("suning_goods/suning_maybe_like", page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SnGoodsListEntity>() {
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
                        snHomeListAdapter.loadMoreFail();

                    }

                    @Override
                    public void onNext(SnGoodsListEntity o) {
                        setGoodsData(o);
                    }
                });
        addSubscription(subscription);

    }

    private void setGoodsData(SnGoodsListEntity entity) {
        if (page <= 0) {
            if (entity.getData().size() == 0) {
                snHomeListAdapter.loadMoreEnd();
                varyViewUtils.showEmptyView();
            } else {
                varyViewUtils.showDataView();
                goodsList.clear();
                goodsList.addAll(entity.getData());
                snHomeListAdapter.setNewData(goodsList);
                if (entity.getData().size() < Constants.PAGE_COUNT) {
                    snHomeListAdapter.loadMoreEnd();
                } else {
                    snHomeListAdapter.loadMoreComplete();
                }
            }

        } else {
            goodsList.addAll(entity.getData());
            snHomeListAdapter.addData(entity.getData());
            if (entity.getData().size() < Constants.PAGE_COUNT) {
                snHomeListAdapter.loadMoreEnd();
            } else {
                snHomeListAdapter.loadMoreComplete();
            }

        }
    }
}

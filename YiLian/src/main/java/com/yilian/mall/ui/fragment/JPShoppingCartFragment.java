package com.yilian.mall.ui.fragment;/**
 * Created by  on 2017/2/25 0025.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.adapter.LeDouListAdapter;
import com.yilian.mall.adapter.ShoppingCartExpandableListViewAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.http.ShoppingCartNetRequest;
import com.yilian.mall.ui.FavoriteActivity;
import com.yilian.mall.ui.JPCommitOrderActivity;
import com.yilian.mall.ui.JPNewCommDetailActivity;
import com.yilian.mall.ui.LoadingActivity;
import com.yilian.mall.ui.LoadingShopCarActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.pay.PayFrom;
import com.yilian.networkingmodule.entity.DataBean;
import com.yilian.networkingmodule.entity.LeDouHomePageDataEntity;
import com.yilian.networkingmodule.entity.ShoppingCartListEntity2;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by  on 2017/2/25 0025.
 */
public class JPShoppingCartFragment extends JPBaseFragment implements View.OnClickListener, ShoppingCartExpandableListViewAdapter.CheckInterface {
    //选中全球购商品总价 单位分
    public float globalBuyPrice = 0;
    //    选中全球购商品数量
    public int globalGoodsCount = 0;
    //商品的状态
    public int goods_status = 0;
    public float totalPrice;//总市场价
    public float totalCost;//总售价
    public int totalCount;//总数量
    public float totalQuan;//总抵扣券（总是厂家-总售价）
    public float totalGrivingPrice;//总零购券
    public float totalReturnIntegral;//总销售奖券
    public float totalReturnLedou;//总区块益豆
    /**
     * 判断是否刷新的标识
     */
    boolean isRefresh = false;
    ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> adapterList = new ArrayList<>();
    ArrayList<JPGoodsEntity> recommendGoodsList = new ArrayList<>();
    private View view;
    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private com.yilian.mall.widgets.NoScrollExpandableListView exListView;
    private TextView tvCollect;
    private RecyclerView recyclerView;
    private LinearLayout llRecommended;
    private com.pulltorefresh.library.PullToRefreshScrollView llContent;
    private CheckBox allChekbox;
    private Button tvGoToPay;
    private LinearLayout llInfo;
    private Button tvSave;
    private Button tvDelete;
    private LinearLayout llShar;
    private LinearLayout llBottom;
    private ShoppingCartExpandableListViewAdapter expandableListViewAdapter;
    private TextView tvCost;
    private TextView tvDiKouQuan;
    private TextView tvLingGouQuan, tvBean;
    private ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> allLists = new ArrayList<>();//所有选中商品的集合
    private TextView tvBuyIntegral;
    private int totalIntegral;
    private boolean isFirst = true;
    private ShoppingCartNetRequest shoppingCartNetRequest;
    private int flag = 0;
    private float totalGavePower;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isFirst = false;
                                shoppingCartLoading();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Logger.i("走了购物车的onResume走了这里");
        }
    }

    @Override
    protected void loadData() {
    }

    /**
     * 加载有loading的购物车列表
     * 适用：第一次进入购物车、有商品添加到购物车
     */
    private void shoppingCartLoading() {
        showDialog();
        getShoppingCartList();
        Logger.i("请求回来后获得数据：shoppingCartLoading");
    }

    private void showDialog() {
        Logger.i("2017年12月13日 14:09:01-走了show-cart");
        startActivity(new Intent(mContext, LoadingShopCarActivity.class));
    }

    /**
     * 获取购物车数据列表
     */
    @SuppressWarnings("unchecked")
    private void getShoppingCartList() {
        Logger.i("ray6666666666-走了购物车获取数据的接口");
        Logger.i("2017年12月13日 14:09:01-走了购物车获取数据的接口");
        netRequestVerify();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getShoppingCartList("cart/cart_list_v2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShoppingCartListEntity2>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showRecommendGoodsList();
                        llContent.onRefreshComplete();
                        showToast(e.getMessage());
                        stopDialog();
                    }

                    @Override
                    public void onNext(ShoppingCartListEntity2 o) {
                        ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list = o.list;
                        //购物车数据为0
                        if (list.size() < 0) {
                            showRecommendGoodsList();
                            return;
                        }

                        Logger.i("请求回来后获得数据：" + adapterList.toString());
                        // 购物车数据不为0
                        adapterList.clear();
                        adapterList.addAll(list);
                        showShoppingCartGoodsList(adapterList);
                        llContent.onRefreshComplete();
                        stopDialog();
                    }

                });
        addSubscription(subscription);
//        shoppingCartNetRequest.getShoppingCartList2(new RequestCallBack<ShoppingCartListEntity2>() {
//
//            @Override
//            public void onSuccess(ResponseInfo<ShoppingCartListEntity2> responseInfo) {
//                ShoppingCartListEntity2 result = responseInfo.result;
//                switch (result.code) {
//                    case 1:
//                        ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list = result.list;
//                        //购物车数据为0
//                        if (list.size() < 0) {
//                            showRecommendGoodsList();
//                            return;
//                        }
//
//                        Logger.i("请求回来后获得数据：" + adapterList.toString());
//                        // 购物车数据不为0
//                        adapterList.clear();
//                        adapterList.addAll(list);
//                        showShoppingCartGoodsList(adapterList);
//                        break;
//                    case -3:
//                        showToast(R.string.system_busy);
//                        showRecommendGoodsList();
//                        break;
//                    default:
//                        showRecommendGoodsList();
//                        break;
//                }
//                llContent.onRefreshComplete();
//
//                stopDialog();
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                showRecommendGoodsList();
//                llContent.onRefreshComplete();
//                showToast(R.string.net_work_not_available);
//
//                stopDialog();
//            }
//        });
    }

    /**
     * netRequest 验证 以及Token设置
     */
    private void netRequestVerify() {
        /**
         * 获取购物车列表的网络请求初始化，如果是登录状态 token 是本地存储的token
         * 如果不是登录状态 token = 0;
         */
        if (shoppingCartNetRequest == null) {
            shoppingCartNetRequest = new ShoppingCartNetRequest(mContext);
        }

        if (isLogin()) {
            shoppingCartNetRequest.setToken(RequestOftenKey.getToken(mContext));
        } else {
            shoppingCartNetRequest.setToken("0");
        }
    }

    /**
     * 隐藏购物车列表，显示推荐商品列表
     */
    private void showRecommendGoodsList() {
        tvRight.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        exListView.setVisibility(View.GONE);//隐藏购物车数据列表
        llRecommended.setVisibility(View.VISIBLE);//显示推荐商品列表
        getRecommendGoodsList();
    }

    private void stopDialog() {
        Logger.i("2017年12月13日 14:09:01-走了stop-cart");
        AppManager.getInstance().killActivity(LoadingShopCarActivity.class);
        AppManager.getInstance().killActivity(LoadingActivity.class);
    }

    /**
     * 隐藏推荐商品列表，显示购物车列表
     *
     * @param list
     */
    private void showShoppingCartGoodsList(ArrayList<ArrayList<ShoppingCartListEntity2.ListBean>> list) {
        tvRight.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        exListView.setVisibility(View.VISIBLE);//隐藏购物车数据列表
        llRecommended.setVisibility(View.GONE);//显示推荐商品列表
        if ((null != list && list.size() > 0)) {
            if (expandableListViewAdapter == null) {
                expandableListViewAdapter = new ShoppingCartExpandableListViewAdapter(list, getContext(), shoppingCartNetRequest, this);
                expandableListViewAdapter.setCheckInterface(this);
                exListView.setAdapter(expandableListViewAdapter);

            } else {
                expandableListViewAdapter.notifyDataSetChanged();
            }
            expandListView();
            calculate();
        } else {
            showRecommendGoodsList();
        }
    }

    /**
     * 获取推荐商品
     */
    private void getRecommendGoodsList() {
        llContent.scrollTo(0, 0);
        Logger.i("shopCarList.size()-" + recommendGoodsList.size());
        if (recommendGoodsList.size() > 0) {
            //如果状态未改变 那么购物车界面再次可见时则不请求新的数据
            return;
        }
        //请求推荐数据
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getLeDouHomePageData("recommend_goods_power_list", 0, Constants.PAGE_COUNT_10,
                        0, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        LeDouListAdapter adapter = new LeDouListAdapter(R.layout.item_gride_cal_power, ((LeDouHomePageDataEntity) o)
                                .data);
                        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
                        recyclerView.addItemDecoration(decor);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                DataBean item = (DataBean) adapter.getItem(position);
                                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);
                                if (item != null) {
                                    intent.putExtra(Constants.INTENT_KEY_JP_GOODS_ID, item.goodsId);
                                    intent.putExtra("classify", item.goodsType);
                                }
                                mContext.startActivity(intent);
                            }
                        });
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 展开列表
     */
    private void expandListView() {
        for (int i = 0; i < expandableListViewAdapter.getGroupCount(); i++) {
            exListView.expandGroup(i);
        }
    }

    /**
     * 计算选中商品价格
     */
    public void calculate() {
        clearData();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < adapterList.size(); i++) {
            ArrayList<ShoppingCartListEntity2.ListBean> listBeen = adapterList.get(i);
            ArrayList<ShoppingCartListEntity2.ListBean> lists = new ArrayList<>();
            for (int j = 0; j < listBeen.size(); j++) {
                ShoppingCartListEntity2.ListBean listBean = listBeen.get(j);
                if (listBean.isChecked) {
                    lists.add(listBean);
                    //计算总共需要的奖券
                    switch (listBean.type) {
                        case "0"://普通商品
                            totalCost += listBean.goodsCost * listBean.goodsCount;
                            totalPrice += listBean.goodsPrice * listBean.goodsCount;
                            totalReturnIntegral += listBean.returnIntegral * listBean.goodsCount;
                            sb.append("0");
                            break;
                        case "1"://易划算
                            totalIntegral += listBean.goodsIntegral * listBean.goodsCount;
                            sb.append("1");
                            break;
                        case "2"://奖券购
                            totalCost += listBean.goodsRetail * listBean.goodsCount;
                            totalIntegral += (listBean.goodsPrice - listBean.goodsRetail) * listBean.goodsCount;
                            sb.append("2");
                            break;
//                      区块益豆
                        case "3":
                        case "4":
                        case GoodsType.CAL_POWER:
                            totalCost += listBean.goodsCost * listBean.goodsCount;
                            totalPrice += listBean.goodsPrice * listBean.goodsCount;
                            totalReturnLedou += listBean.returnBean * listBean.goodsCount;
                            totalGavePower += listBean.subsidy * listBean.goodsCount;
                            sb.append(listBean.type);
                            break;
                        default:
                            break;
                    }
                    totalCount += listBean.goodsCount;
                    if ("1".equals(listBean.isGlobal)) {
                        globalBuyPrice += listBean.goodsCount * listBean.goodsCost;//获取全球购商品总价，单位分
                        globalGoodsCount += listBean.goodsCount;//获取全球购商品数量
                    }
                    //计算商品状态
                    goods_status += listBean.goodsStatus;
                }

            }
            if (lists.size() > 0) {
                allLists.add(lists);
            }
        }

        tvCost.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(totalCost)));//支付的钱数

        String str = sb.toString();
//        if (!str.contains("3") && !str.contains("4") && !str.contains("5")) {
//            if (totalReturnIntegral == 0) {
//                tvLingGouQuan.setVisibility(View.GONE);
//            } else {
//                tvLingGouQuan.setVisibility(View.VISIBLE);
//            }
//            tvLingGouQuan.setText(getResources().getString(R.string.back_score) + MoneyUtil.getLeXiangBiNoZero(totalReturnIntegral));//支付的零购券
//            tvBean.setVisibility(View.GONE);
//        } else {
//
//        }
        tvLingGouQuan.setVisibility(View.GONE);
        tvBean.setVisibility(View.VISIBLE);
        if (totalGavePower == 0) {
            tvBean.setText("送" + MoneyUtil.getLeXiangBiNoZero(totalReturnLedou));
        } else {
            tvBean.setText("送" + MoneyUtil.getLeXiangBiNoZero(totalReturnLedou) + "+平台加赠" + MoneyUtil.getLeXiangBiNoZero(totalGavePower));
        }
        tvGoToPay.setText("去结算(" + totalCount + ")");
    }

    /**
     *         //获取选中集合前，先初始化数据，防止数据异常
     */
    private void clearData() {
        totalPrice = 0f;
        totalCost = 0f;
        totalQuan = 0f;
        totalGrivingPrice = 0f;
        totalReturnIntegral = 0f;
        totalReturnLedou = 0f;
        totalCount = 0;
        globalBuyPrice = 0;
        globalGoodsCount = 0;
        totalIntegral = 0;
        goods_status = 0;
        totalGavePower=0;
        allLists.clear();//获取选中集合先，先把该集合清空，避免数据重复
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_jpshopping_cart, container, false);
        }
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        Logger.i("onRsume-" + "Fragment-ShopCar");
        super.onResume();

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_SHOP_FRAGMENT, mContext, false);
        Logger.i("走了购物车的onResume" + isRefresh);

        if (isRefresh == true) {
            initEditor();
            shoppingCartNotLoading();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_SHOP_FRAGMENT, isRefresh, mContext);
        }
    }

    private void initEditor() {
        llShar.setVisibility(View.GONE);
        llInfo.setVisibility(View.VISIBLE);
        tvRight.setText("编辑");
        flag = 0;
    }

    private void shoppingCartNotLoading() {
        getShoppingCartList();
    }

    private void initView(View view) {
        tvCost = (TextView) view.findViewById(R.id.tv_buy_price);
        tvBuyIntegral = (TextView) view.findViewById(R.id.tv_buy_integral);
        tvDiKouQuan = (TextView) view.findViewById(R.id.tv_quan);
        tvLingGouQuan = (TextView) view.findViewById(R.id.tv_giving_price);
        tvBean = (TextView) view.findViewById(R.id.tv_giving_bean);

        tvLeft1 = (TextView) view.findViewById(R.id.tv_left1);
        tvLeft1.setOnClickListener(this);
        rlTvLeft1 = (RelativeLayout) view.findViewById(R.id.rl_tv_left1);
        rlTvLeft1.setOnClickListener(this);
        ivLeft1 = (ImageView) view.findViewById(R.id.iv_left1);
        ivLeft1.setVisibility(View.GONE);
        ivLeft1.setOnClickListener(this);
        rlIvLeft1 = (RelativeLayout) view.findViewById(R.id.rl_iv_left1);
        rlIvLeft1.setOnClickListener(this);
        ivLeft2 = (ImageView) view.findViewById(R.id.iv_left2);
        ivLeft2.setOnClickListener(this);
        rlIvLeft2 = (RelativeLayout) view.findViewById(R.id.rl_iv_left2);
        rlIvLeft2.setOnClickListener(this);
        ivTitle = (ImageView) view.findViewById(R.id.iv_title);
        ivTitle.setVisibility(View.GONE);
        ivTitle.setOnClickListener(this);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText("购物车");
        tvTitle.setVisibility(View.VISIBLE);//头部中间TextView显示出来
        tvTitle.setOnClickListener(this);
        ivRight1 = (ImageView) view.findViewById(R.id.iv_right1);
        ivRight1.setOnClickListener(this);
        ivRight2 = (ImageView) view.findViewById(R.id.iv_right2);
        ivRight2.setOnClickListener(this);
        rlIvRight2 = (RelativeLayout) view.findViewById(R.id.rl_iv_right2);
        rlIvRight2.setVisibility(View.GONE);
        rlIvRight2.setOnClickListener(this);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        tvRight.setTextColor(mContext.getResources().getColor(R.color.color_red));
        tvRight.setText("编辑");
        tvRight.setOnClickListener(this);
        rlTvRight = (RelativeLayout) view.findViewById(R.id.rl_tv_right);
        rlTvRight.setOnClickListener(this);
        viewLine = (View) view.findViewById(R.id.view_line);
        viewLine.setOnClickListener(this);
        llJpTitle = (LinearLayout) view.findViewById(R.id.ll_jp_title);
        llJpTitle.setOnClickListener(this);
        exListView = (com.yilian.mall.widgets.NoScrollExpandableListView) view.findViewById(R.id.exListView);
        tvCollect = (TextView) view.findViewById(R.id.tv_collect);
        tvCollect.setOnClickListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.gridView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        llRecommended = (LinearLayout) view.findViewById(R.id.ll_recommended);
        llRecommended.setOnClickListener(this);
        llContent = (com.pulltorefresh.library.PullToRefreshScrollView) view.findViewById(R.id.ll_content);
        llContent.setOnClickListener(this);
        allChekbox = (CheckBox) view.findViewById(R.id.all_chekbox);
        allChekbox.setOnClickListener(this);
        tvGoToPay = (Button) view.findViewById(R.id.tv_go_to_pay);
        tvGoToPay.setOnClickListener(this);
        llInfo = (LinearLayout) view.findViewById(R.id.ll_info);
        llInfo.setOnClickListener(this);
        tvSave = (Button) view.findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);
        tvDelete = (Button) view.findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(this);
        llShar = (LinearLayout) view.findViewById(R.id.ll_shar);
        llShar.setOnClickListener(this);
        llBottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        llBottom.setOnClickListener(this);
    }

    private void initListener() {
        exListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//屏蔽点击group折叠分组
            }
        });
        llContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getShoppingCartList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:

                break;
            case R.id.rl_tv_right:
            case R.id.tv_right:
//点击编辑按钮
                if (flag == 0) {
                    showEditor();
                } else if (flag == 1) {
                    hideEditor();
                }
                break;
            case R.id.tv_go_to_pay:
                if (isLogin()) {
                    goCheckOut();
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.tv_save:
                if (isLogin()) {
                    if (TextUtils.isEmpty(getGoodsFilialeSupplierId())) {
                        showToast("还未选择需要收藏的商品");
                        return;
                    }
                    collect(getGoodsFilialeSupplierId());
                } else {
                    showToast("登录后才能进行收藏");
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.tv_delete:
                showDialog(null, "您确定要将这些商品从购物车中移除吗", null, 0, Gravity.CENTER, "确定", "取消", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                doDelete();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                            default:
                                break;
                        }
                    }
                }, mContext);
                break;
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_collect:
                if (isLogin()) {
                    startActivity(new Intent(mContext, FavoriteActivity.class));
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void showEditor() {
        llShar.setVisibility(View.VISIBLE);
        llInfo.setVisibility(View.GONE);
        tvRight.setText("完成");
        changeFlag();
    }

    private void hideEditor() {
        llShar.setVisibility(View.GONE);
        llInfo.setVisibility(View.VISIBLE);
        tvRight.setText("编辑");
        changeFlag();
    }

    //结算
    private void goCheckOut() {

        if (allLists.size() < 1) {
            showToast("没有选中的商品");
        } else {
            Logger.i("选中全球购商品价格;  " + globalBuyPrice + " 全球购的数量 " + globalGoodsCount);
            //全球购商品数量大于1且总价大于200000(单位：分)时，提示用户分开下订单。总价小于2000以及总价大于2000但只有1件商品时，是可以下单的
            if (globalBuyPrice > 200000 && globalGoodsCount > 1) {
                showDialog(null, getResources().getString(R.string.global_total_price_overflow), null, 0, Gravity.CENTER, "确定", null, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, mContext);
                return;
            }
            //如果有已下架 或 已抢光商品
            Logger.i("goods_status:" + goods_status);
            if (goods_status != 0) {
                showToast(R.string.can_not_buy);
                return;
            }
            Intent checkOutIntent = new Intent(mContext, JPCommitOrderActivity.class);
            checkOutIntent.putExtra("OrderType", PayFrom.GOODS_SHOPPING_CART);
            checkOutIntent.putExtra("cartList", allLists);
            startActivity(checkOutIntent);
        }

    }

    /**
     * 获取 移入收藏的商品ID、商家ID拼接字符串
     *
     * @return
     */
    private String getGoodsFilialeSupplierId() {
        StringBuffer sbGoodIds = new StringBuffer();
        StringBuffer sbFilialeIds = new StringBuffer();
        StringBuffer sbSupplierIds = new StringBuffer();
        StringBuffer sbGoodsType = new StringBuffer();
        for (ArrayList<ShoppingCartListEntity2.ListBean> listBean :
                adapterList) {
            for (ShoppingCartListEntity2.ListBean bean :
                    listBean) {
                if (bean.isChecked) {
//                    sbGoodIds.append(bean.cartId);
                    sbGoodIds.append(bean.goodsId);
                    sbGoodIds.append(",");
                    sbFilialeIds.append(bean.filialeId);
                    sbSupplierIds.append(bean.supplierId);
                    if ("4".equals(bean.type)) {
                        sbGoodsType.append("3");
                        sbGoodsType.append(",");
                    } else {
                        sbGoodsType.append(bean.type);
                        sbGoodsType.append(",");
                    }
                }

            }

        }
        deleteLastComma(sbGoodIds);
        deleteLastComma(sbFilialeIds);
        deleteLastComma(sbSupplierIds);
        deleteLastComma(sbGoodsType);
        Logger.i(sbGoodIds.toString() + " sbGoodsType  " + sbGoodsType.toString());
        return sbGoodIds.toString() + ":" + sbFilialeIds.toString() + ":" + sbSupplierIds.toString() + ":" + sbGoodsType.toString();
    }

    /**
     * 移入收藏
     *
     * @param ids goods_id:filialeIds:supplierIds 格式的拼接
     */
    private void collect(String ids) {
        String[] strings = ids.split(":");
        Logger.i("ids" + ids);
        if (strings.length == 0) {
            showToast("还未选择需要收藏的商品");
            return;
        }
        if (strings.length < 3) {
            showToast("选中商品信息异常，可进入商品详情进行收藏");
            return;
        }
        netRequestVerify();
        Logger.i("2018年2月23日 09:50:48-" + strings[0]);
        Logger.i("2018年2月23日 09:50:48-" + strings[1]);
        Logger.i("2018年2月23日 09:50:48-" + strings[2]);
        Logger.i("2018年2月23日 09:50:48-" + strings[3]);

        shoppingCartNetRequest.collectv2(strings[0], strings[1], strings[2], strings[3], new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("收藏成功");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 点击删除按钮，删除选中按钮
     */
    private void doDelete() {
        deleteShoppingCartGoods(deleteOrCheckOutShop());

    }

    /**
     * 点击全选按钮
     */
    private void doCheckAll() {
        for (int i = 0; i < adapterList.size(); i++) {
            ArrayList<ShoppingCartListEntity2.ListBean> listBeen = adapterList.get(i);
            for (int j = 0; j < listBeen.size(); j++) {
                listBeen.get(j).isChecked = allChekbox.isChecked();
            }

        }
        if (expandableListViewAdapter != null) {
            expandableListViewAdapter.notifyDataSetChanged();
        }
        calculate();
    }

    /**
     * 保证flag在0和1之间循环
     */
    private void changeFlag() {
        flag = (flag + 1) % 2;
    }

    /**
     * 删除字符串最后一个逗号
     *
     * @param sb
     */
    private void deleteLastComma(StringBuffer sb) {
        if (sb.toString().endsWith(",")) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /**
     * 删除购物车商品
     */
    private void deleteShoppingCartGoods(String cartId) {

        if (TextUtils.isEmpty(cartId)) {
            showToast("还未选择需要删除的商品");
            return;
        }

        netRequestVerify();

        shoppingCartNetRequest.deleteShoppingCartGoods(cartId, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("删除成功");
                        getShoppingCartList();
                        Logger.i("我这走了完成2");
                        break;

                    default:
                        showToast("购物车删除错误码：" + responseInfo.result.code);
                        break;

                }
                stopDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopDialog();
                showToast("网络连接异常");
            }
        });

    }

    /**
     * 获取 删除的商品ID拼接字符串
     *
     * @return
     */
    private String deleteOrCheckOutShop() {
        StringBuffer sb = new StringBuffer();
        for (ArrayList<ShoppingCartListEntity2.ListBean> listBean :
                adapterList) {
            for (ShoppingCartListEntity2.ListBean bean :
                    listBean) {
                if (bean.isChecked) {
                    sb.append(bean.cartId);
                    sb.append(",");
                }

            }

        }
        deleteLastComma(sb);

        Logger.i(sb.toString());
        return sb.toString();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {

    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        for (int i = 0; i < adapterList.size(); i++) {
            ArrayList<ShoppingCartListEntity2.ListBean> listBeen = adapterList.get(i);
            for (int j = 0; j < listBeen.size(); j++) {
                ShoppingCartListEntity2.ListBean listBean = listBeen.get(j);
                if (listBean.isChecked != isChecked) {
                    allChildSameState = false;
                    break;
                }
            }
        }
        if (allChildSameState) {//如果所有子元素选中状态一样，则全选按钮和这些子元素选中状态一样
            allChekbox.setChecked(isChecked);
        } else {//否则全选按钮就是不选中状态
            allChekbox.setChecked(allChildSameState);
        }
        expandableListViewAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkAll() {
        for (int i = 0; i < adapterList.size(); i++) {
            ArrayList<ShoppingCartListEntity2.ListBean> listBeen = adapterList.get(i);
            for (int j = 0; j < listBeen.size(); j++) {
                ShoppingCartListEntity2.ListBean listBean = listBeen.get(j);
                if (!listBean.isChecked) {
                    allChekbox.setChecked(false);
                    return;
                }
            }
        }
        allChekbox.setChecked(true);
    }
}

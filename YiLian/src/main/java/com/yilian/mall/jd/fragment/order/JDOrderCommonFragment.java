package com.yilian.mall.jd.fragment.order;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.jd.adapter.JDOrderCommonAdapter;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDCommitOrderSuccessEntity;
import com.yilian.networkingmodule.entity.jd.JDOrderListEntity;
import com.yilian.networkingmodule.entity.jd.jdEventBusModel.JDOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 京东订单列表 通用
 *
 * @author Created by Zg on 2018/5/22.
 */
@SuppressLint("ValidFragment")
public class JDOrderCommonFragment extends JPBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    /**
     * 订单类型 0全部
     */
    public static int OrderType_all = 0;
    /**
     * 订单类型 1待付款
     */
    public static int OrderType_payment = 1;
    /**
     * 订单类型 2待收货
     */
    public static int OrderType_receiving = 2;
    /**
     * 订单类型 3已完成
     */
    public static int OrderType_finish = 3;
    /**
     * 订单类型 4已取消
     */
    public static int OrderType_cancel = 4;
    private VaryViewUtils varyViewUtils;
    private int page = Constants.PAGE_INDEX;//页数
    /**
     * 当前订单类型
     */
    private int TYPE = OrderType_all;
    /**
     * 当前是否可见
     */
    private boolean hidden = false;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private JDOrderCommonAdapter mAdapter;
    //系统时间  等待付款时长
    private long marginTime, time;

    public static JDOrderCommonFragment getInstance(int type) {
        JDOrderCommonFragment sf = new JDOrderCommonFragment();
        sf.TYPE = type;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyLoadData() {
        if (varyViewUtils != null) {
            varyViewUtils.showLoadingView();
        }
        getFirstPageData();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.jd_fragment_order_common, null);
        }
        EventBus.getDefault().register(this);
        initView();
        initListener();
        return rootView;
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewByJDOrderList(R.id.vary_content, rootView, new RefreshClickListener());

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new JDOrderCommonAdapter();
        mRecyclerView.setAdapter(mAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 500);


    }

    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
                mAdapter.setEnableLoadMore(false);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JDOrderListEntity.DataBean bean = (JDOrderListEntity.DataBean) adapter.getItem(position);
                if (bean != null) {
                    JumpJdActivityUtils.toJDOrderDetails(mContext, bean.jdOrderId);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                JDOrderListEntity.DataBean bean = (JDOrderListEntity.DataBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.rv_goods_list:
                        if (bean != null) {
                            JumpJdActivityUtils.toJDOrderDetails(mContext, bean.jdOrderId);
                        }
                        break;
                    case R.id.ll_order_delete:
                        //删除订单
                        deleteOrder(bean.jdOrderId, position);
                        break;
                    case R.id.tv_buy_again:
                        //再次购买
                        jdBuyAgain(bean.jdType,bean.goodsist);
                        break;
                    case R.id.tv_confirm:
                        //确认收货
                        confirmGoods(bean.jdOrderId, position);
                        break;
                    case R.id.tv_apply_balance:
                        //申请结算
                        applyOrderSettle(bean, position);
                        break;
                    case R.id.tv_pay:
                        //去支付
                        JDCommitOrderSuccessEntity jdCommitOrderSuccessEntity = new JDCommitOrderSuccessEntity();
                        jdCommitOrderSuccessEntity.goodsPrice = String.valueOf(bean.orderJdPrice);
                        jdCommitOrderSuccessEntity.freight = String.valueOf(bean.freight);
                        jdCommitOrderSuccessEntity.orderId = bean.jdOrderId;
                        jdCommitOrderSuccessEntity.orderIndex = bean.orderIndex;
                        jdCommitOrderSuccessEntity.countDown = String.valueOf(time - (System.currentTimeMillis() / 1000 + marginTime - bean.orderTime));
//                        jdCommitOrderSuccessEntity.daiGouQuanMoney = bean.orderDaiGouQuanMoney;
//                        daigouquan 代购券取消
                        jdCommitOrderSuccessEntity.daiGouQuanMoney = "0";
                        if(bean.jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD){
                            //购物卡 京东收银台
                            JumpCardActivityUtils.toJDCashDeskActivity(mContext, jdCommitOrderSuccessEntity);
                        }else {
                            JumpJdActivityUtils.toJDCashDeskActivity(mContext, jdCommitOrderSuccessEntity);
                        }
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private void getFirstPageData() {
        page = Constants.PAGE_INDEX;
        getOrdersList();
    }

    /**
     * 删除订单
     *
     * @param jdOrderId 京东订单号
     * @param position  点击item
     */
    private void deleteOrder(String jdOrderId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().getTopActivity());
        builder.setMessage("您是否要删除该订单？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                        jdDeleteOrder("jd_orders/jd_delete_order", jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HttpResultBean>() {
                            @Override
                            public void onCompleted() {
                                stopMyDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                stopMyDialog();
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResultBean bean) {
                                mAdapter.remove(position);
                                showToast(bean.msg);
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 京东再次购买
     * @param jdType  类型 0普通京东商品 1购物卡京东商品
     */
    private void jdBuyAgain(int jdType,List<JDOrderListEntity.GoodsList> goodsLists) {
        startMyDialog();
        StringBuilder goods_counts = new StringBuilder("");
        StringBuilder goods_skus = new StringBuilder("");
        for (int i = 0; i < goodsLists.size(); i++) {
            if (i < goodsLists.size() - 1) {
                goods_counts.append(goodsLists.get(i).getSkuNum()).append(",");
            } else {
                goods_counts.append(String.valueOf(goodsLists.get(i).getSkuNum()));
            }
            if (i < goodsLists.size() - 1) {
                goods_skus.append(goodsLists.get(i).getSkuId()).append(",");
            } else {
                goods_skus.append(String.valueOf(goodsLists.get(i).getSkuId()));
            }
        }
        String urlKey = "jd_cart/cart_add_more";
        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
            urlKey = "jd_card_cart/card_cart_add_more";
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                setJDBuyAgain(urlKey, goods_counts.toString(), goods_skus.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        if (jdType == JumpToOtherPageUtil.JD_GOODS_TYPE_CARD) {
                            JumpCardActivityUtils.toCardJdShoppingList(mContext, goods_skus.toString());
                        } else {
                            JumpJdActivityUtils.toJdHomePageActivity(mContext, Constants.JD_INDEX_SHOPPING_CAR, goods_skus.toString());
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 确认收货
     *
     * @param jdOrderId 京东订单号
     * @param position  点击item
     */
    private void confirmGoods(String jdOrderId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().getTopActivity());
        builder.setMessage("您是否收到该订单商品？");
        builder.setNegativeButton("未收货", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("已收货", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                        jdConfirmGoods("jd_orders/jd_confirm_goods", jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HttpResultBean>() {
                            @Override
                            public void onCompleted() {
                                stopMyDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                stopMyDialog();
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResultBean bean) {
                                mAdapter.remove(position);
                                JumpJdActivityUtils.toJDOrderReceivingSuccess(mContext);
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 申请结算益豆
     */
    @SuppressWarnings("unchecked")
    private void applyOrderSettle(JDOrderListEntity.DataBean dataBean, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("请在收到商品并确认无质量问题，且无需退换货之后，再确认申请益豆，确认后赠送的益豆立即到账，且购买的商品不可申请退款！");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startMyDialog();
                Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                        jdApplyOrderSettle("jd_orders/jd_apply_order_settle", dataBean.orderIndex).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HttpResultBean>() {
                            @Override
                            public void onCompleted() {
                                stopMyDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                stopMyDialog();
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onNext(HttpResultBean bean) {
                                showToast(bean.msg);
                                //结算状态 1已结算
                                dataBean.settleStatus = 1;
                                // 0 不可申请结算
                                dataBean.applySettle = 0;
                                mAdapter.setData(position, dataBean);
                            }
                        });
                addSubscription(subscription);
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    /**
     * 订单列表
     */
    private void getOrdersList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getJDOrderList("jd_orders/jd_order_list", TYPE, page, Constants.PAGE_COUNT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDOrderListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (page == Constants.PAGE_INDEX) {
                            varyViewUtils.showErrorView();
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDOrderListEntity jdOrderListEntity) {
                        List<JDOrderListEntity.DataBean> data = jdOrderListEntity.data;
                        long timeStampSec = System.currentTimeMillis() / 1000;
                        marginTime = jdOrderListEntity.systemTime - timeStampSec;

                        time = jdOrderListEntity.time;

                        if (page == Constants.PAGE_INDEX) {
                            if (data.size() <= 0) {
                                varyViewUtils.showEmptyView();
                            } else {
                                mAdapter.setNewData(data);
                                mRecyclerView.scrollToPosition(0);
                                varyViewUtils.showDataView();
                            }
                        } else {
                            mAdapter.addData(data);
                        }
                        if (data.size() < Constants.PAGE_COUNT) {
                            mAdapter.loadMoreEnd();
                        }
                    }
                });
        addSubscription(subscription);
    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.head_ll_praise://热帖 点赞
//                setFirstHotPostFabulous(firstHotPost.getPostID());
//                break;
//            default:
//                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        Logger.i("getShopsList：onLoadMoreRequested");
        getNextPageData();
        swipeRefreshLayout.setEnabled(false);
    }

    private void getNextPageData() {
        page++;
        getOrdersList();
    }

    @Subscribe()
    public void receiveEvent(JDOrderListUpdateModel resultModel) throws PackageManager.NameNotFoundException {
        if (TYPE == OrderType_all) {
            if (resultModel.handleType == JDOrderListUpdateModel.HandleType_pay) {
                //去支付
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        JDOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 待收获
                        item.type = 2;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == JDOrderListUpdateModel.HandleType_receiving) {
                //确认收货
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        JDOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 已完成
                        item.type = 3;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == JDOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        JDOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 已取消
                        item.type = 4;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == JDOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            } else if (resultModel.handleType == JDOrderListUpdateModel.HandleType_add) {
                //添加新订单
                getFirstPageData();
            }
        } else if (TYPE == OrderType_payment) {
            if (resultModel.handleType == JDOrderListUpdateModel.HandleType_pay) {
                //去支付
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            } else if (resultModel.handleType == JDOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        JDOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 已取消
                        item.type = 4;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == JDOrderListUpdateModel.HandleType_add) {
                //添加新订单
                getFirstPageData();
            }
        } else if (TYPE == OrderType_receiving) {
            if (resultModel.handleType == JDOrderListUpdateModel.HandleType_receiving) {
                //确认收货
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            }
        } else if (TYPE == OrderType_finish) {
            if (resultModel.handleType == JDOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            }
        } else if (TYPE == OrderType_cancel) {
            if (resultModel.handleType == JDOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).jdOrderId.equals(resultModel.jdOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrdersList();
                }
            }, 1000);
        }
    }
}
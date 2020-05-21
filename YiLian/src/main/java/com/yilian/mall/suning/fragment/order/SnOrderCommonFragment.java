package com.yilian.mall.suning.fragment.order;

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
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.adapter.SnOrderCommonAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.MyBigDecimal;
import com.yilian.networkingmodule.entity.suning.SnCommitOrderEntity;
import com.yilian.networkingmodule.entity.suning.SnOrderListEntity;
import com.yilian.networkingmodule.entity.suning.snEventBusModel.SnOrderListUpdateModel;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 苏宁订单列表 通用
 *
 * @author Created by Zg on 2018/5/22.
 */
@SuppressLint("ValidFragment")
public class SnOrderCommonFragment extends JPBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
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


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SnOrderCommonAdapter mAdapter;

    //系统时间与本地时间的差值   等待付款时长
    private long marginTime, time;

    public static SnOrderCommonFragment getInstance(int type) {
        SnOrderCommonFragment sf = new SnOrderCommonFragment();
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
        for (Map.Entry<Integer, Subscription> integerSubscriptionEntry : mAdapter.countDownsMap.entrySet()) {
            integerSubscriptionEntry.getValue().unsubscribe();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void lazyLoadData() {
        if (varyViewUtils != null) {
            varyViewUtils.showLoadingView();
        }
        getFirstPageData();
    }

    private void getFirstPageData() {
        page = Constants.PAGE_INDEX;
        getOrdersList();
    }

    /**
     * 订单列表
     */
    private void getOrdersList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getSnOrderList("suning_orders/suning_order_list", TYPE, page, Constants.PAGE_COUNT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnOrderListEntity>() {
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
                    public void onNext(SnOrderListEntity orderListEntity) {
                        List<SnOrderListEntity.DataBean> data = orderListEntity.data;
                        long timeStampSec = System.currentTimeMillis() / 1000;
                        marginTime = orderListEntity.systemTime - timeStampSec;
                        time = orderListEntity.time;

                        if (page == Constants.PAGE_INDEX) {
                            if (data.size() <= 0) {
                                varyViewUtils.showEmptyView();
                            } else {
                                mAdapter.clearHolderList();
                                mAdapter.setNewData(data);
                                mAdapter.setSysTime(orderListEntity.systemTime, orderListEntity.time, marginTime);
                                mRecyclerView.scrollToPosition(0);
                                varyViewUtils.showDataView();
                            }
                        } else {
                            if (data.size() <= 0 || data.size() < Constants.PAGE_COUNT) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.addData(data);
                            }
                        }
                    }
                });
        addSubscription(subscription);
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
        String emptyMsg = "您还没有订单哦";
        if (TYPE == OrderType_all) {
            emptyMsg = "您还没有任何订单哦";
        } else if (TYPE == OrderType_payment) {
            emptyMsg = "您还没有待支付的订单哦";
        } else if (TYPE == OrderType_receiving) {
            emptyMsg = "您还没有待收货的订单哦";
        } else if (TYPE == OrderType_finish) {
            emptyMsg = "您还没有已完成的订单哦";
        } else if (TYPE == OrderType_cancel) {
            emptyMsg = "您还没有已取消的订单哦";
        }
        varyViewUtils.setVaryViewBySnOrderList(R.id.vary_content, rootView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFirstPageData();
                    }
                }, 1000);
            }
        }, emptyMsg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_HOME, null);
            }
        });

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SnOrderCommonAdapter();
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
                SnOrderListEntity.DataBean bean = (SnOrderListEntity.DataBean) adapter.getItem(position);
                JumpSnActivityUtils.toSnOrderDetails(mContext, bean.snOrderId);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SnOrderListEntity.DataBean bean = (SnOrderListEntity.DataBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_delete:
                        //删除订单
                        deleteOrder(bean.snOrderId, position);
                        break;
                    case R.id.tv_buy_again:
                        //再次购买
                        buyAgain(bean.goodsist);
                        break;
                    case R.id.tv_cancel:
                        //取消订单
                        cancelOrder(bean.snOrderId, position);
                        break;
                    case R.id.tv_apply_balance:
                        //申请结算
                        if (bean.settleStatus == 0 && bean.applySettle == 1) {
                            applyOrderSettle(bean, position);
                        }
                        break;
                    case R.id.tv_pay:
                        //去支付
                        SnCommitOrderEntity snCommitOrderEntity = new SnCommitOrderEntity();
                        snCommitOrderEntity.orderId = bean.orderId;
                        snCommitOrderEntity.snOrderId = bean.snOrderId;
//                        snCommitOrderEntity.amount = MyBigDecimal.add(MyBigDecimal.sub(String.valueOf(bean.payment), String.valueOf(bean.freight)), bean.coupon);
                        snCommitOrderEntity.amount = MyBigDecimal.sub(String.valueOf(bean.payment), String.valueOf(bean.freight));
                        snCommitOrderEntity.freight = String.valueOf(bean.freight);
                        snCommitOrderEntity.id = bean.orderIndex;
//                        snCommitOrderEntity.daiGouQuanMoney = bean.coupon;
                        snCommitOrderEntity.daiGouQuanMoney = "0";
                        //剩余支付时间 = 等待支付限制时间 - （当前服务器时间 - 订单时间）
                        // marginTime 当时本地时间和当时服务器时间的差值
                        //当前服务器时间 = 当前本地时间 + 当时本地时间和当时服务器时间的差值;
                        //剩余支付时间 = 等待支付限制时间 - （当前本地时间 + 当时本地时间和当时服务器时间的差值 - 订单时间）
                        snCommitOrderEntity.time = String.valueOf(time - (System.currentTimeMillis() / 1000 + marginTime - bean.orderTime));
                        JumpSnActivityUtils.toSnCashDeskActivity(mContext, snCommitOrderEntity);
                        break;
                    default:
                        break;
                }
            }
        });


    }

    /**
     * 删除订单
     *
     * @param snOrderId 苏宁订单号
     * @param position  点击item
     */
    private void deleteOrder(String snOrderId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        snDeleteOrder("suning_orders/suning_delete_order", snOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
     * 再次购买
     */
    private void buyAgain(List<SnOrderListEntity.GoodsList> goodsLists) {
        startMyDialog();
        StringBuilder goodsCounts = new StringBuilder("");
        StringBuilder goodsSkus = new StringBuilder("");
        for (int i = 0; i < goodsLists.size(); i++) {
            if (i < goodsLists.size() - 1) {
                goodsCounts.append(goodsLists.get(i).getSkuNum()).append(",");
            } else {
                goodsCounts.append(String.valueOf(goodsLists.get(i).getSkuNum()));
            }
            if (i < goodsLists.size() - 1) {
                goodsSkus.append(goodsLists.get(i).getSkuId()).append(",");
            } else {
                goodsSkus.append(String.valueOf(goodsLists.get(i).getSkuId()));
            }
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                putInShoppingCartMore("suning_cart/suning_cart_add_more", goodsCounts.toString(), goodsSkus.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("加入购物车成功，是否去结算？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("去购物车", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_CART, goodsSkus.toString());
                            }
                        });
                        builder.create();
                        builder.setCancelable(false);
                        builder.show();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 取消订单
     *
     * @param snOrderId 苏宁订单号
     * @param position  点击item
     */
    private void cancelOrder(String snOrderId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("您是否要取消该订单？");
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
                        snCancleOrder("suning_orders/suning_cancle_order", snOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                                //如果是全部列表 设置该订单状态为已取消，不是全部列表 则移除该订单
                                if (TYPE == OrderType_all) {
                                    SnOrderListEntity.DataBean mData = mAdapter.getData().get(position);
                                    // order_status: "1",  订单状态 1:审核中; 2:待发货; 3:待收货; 4:已完成; 5:已取消; 6:已退货; 7:待处理; 8：审核不通过，订单已取消; 9：待支付
                                    mData.orderStatus = 5;
                                    mAdapter.setData(position, mData);
                                } else {
                                    mAdapter.remove(position);
                                }
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
     * 申请结算益豆
     */
    @SuppressWarnings("unchecked")
    private void applyOrderSettle(SnOrderListEntity.DataBean dataBean, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //是否大于等于7天 1 是 0 否 判断是否执行申请结算请求
        if (dataBean.is7day == 1) {
            //大于7天可申请售后
            builder.setMessage("请在收到商品并确认无质量问题，且无需退换货之后，再确认申请益豆，确认后赠送的乐豆立即到账，且购买的商品不可申请退款！");
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
                            snApplyOrderSettle("suning_orders/suning_apply_order_settle", dataBean.orderIndex).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        } else {
            //小于7天可申请售后
            builder.setMessage("确认收货7天之后，可以申请益豆，请在七天后申请。");
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create();
        builder.setCancelable(false);
        builder.show();

    }

    @Override
    public void onLoadMoreRequested() {
        Logger.i("getShopsList：onLoadMoreRequested");
        getNextPageData();
        swipeRefreshLayout.setEnabled(false);
    }    //按钮的点击事件

    private void getNextPageData() {
        page++;
        getOrdersList();
    }

    @Subscribe()
    public void receiveEvent(SnOrderListUpdateModel resultModel) throws PackageManager.NameNotFoundException {
        if (TYPE == OrderType_all) {
            if (resultModel.handleType == SnOrderListUpdateModel.HandleType_pay) {
                //去支付
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        SnOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 待发货
                        item.orderStatus = 2;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == SnOrderListUpdateModel.HandleType_receiving) {
                //确认收货
                getFirstPageData();
            } else if (resultModel.handleType == SnOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        SnOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 已取消
                        item.orderStatus = 5;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == SnOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            } else if (resultModel.handleType == SnOrderListUpdateModel.HandleType_add) {
                //添加新订单
                getFirstPageData();
            }
        } else if (TYPE == OrderType_payment) {
            if (resultModel.handleType == SnOrderListUpdateModel.HandleType_pay) {
                //去支付
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            } else if (resultModel.handleType == SnOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        SnOrderListEntity.DataBean item = mAdapter.getData().get(i);
                        //改变订单状态为 已取消
                        item.orderStatus = 5;
                        mAdapter.setData(i, item);
                        break;
                    }
                }
            } else if (resultModel.handleType == SnOrderListUpdateModel.HandleType_add) {
                //添加新订单
                getFirstPageData();
            }
        } else if (TYPE == OrderType_receiving) {
            if (resultModel.handleType == SnOrderListUpdateModel.HandleType_receiving) {
                //确认收货
                getFirstPageData();
            }
        } else if (TYPE == OrderType_finish) {
            if (resultModel.handleType == SnOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            }
        } else if (TYPE == OrderType_cancel) {
            if (resultModel.handleType == SnOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).snOrderId.equals(resultModel.snOrderId)) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            }
        }
    }

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


}
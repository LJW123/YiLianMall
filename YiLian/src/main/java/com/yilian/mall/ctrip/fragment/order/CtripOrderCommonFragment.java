package com.yilian.mall.ctrip.fragment.order;

import android.annotation.SuppressLint;
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
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripOrderCommonAdapter;
import com.yilian.mall.ctrip.popupwindow.CtripWarnCommonPopwindow;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripCommitOrderEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderListEntity;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderShareEntity;
import com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel.CtripOrderListUpdateModel;
import com.yilian.networkingmodule.entity.ctrip.ctripMultiItem.CtripOrderListLayoutType;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 携程订单列表 通用
 *
 * @author Created by Zg on 2018/8/20.
 */
@SuppressLint("ValidFragment")
public class CtripOrderCommonFragment extends JPBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    /**
     * 订单类型 全部
     */
    public static String OrderType_all = "0";
    /**
     * 订单类型 未提交
     */
    public static String OrderType_submit = "Uncommitted";
    /**
     * 订单类型 确认中
     */
    public static String OrderType_process = "Process";
    /**
     * 订单类型 已确认
     */
    public static String OrderType_affirm = "Confirm";
    /**
     * 订单类型 已消费
     */
    public static String OrderType_expense = "Success";
    /**
     * 订单类型 已取消
     */
    public static String OrderType_cancel = "Cancel";

    private VaryViewUtils varyViewUtils;
    private int page = Constants.PAGE_INDEX;//页数
    /**
     * 当前订单类型
     */
    private String TYPE = OrderType_all;

    /**
     * 当前日期
     */
    private String currentDate;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CtripOrderCommonAdapter mAdapter;
    /**
     * 弹出窗口
     */
    private CtripWarnCommonPopwindow reminderPop, orderCancelPop, orderDeletePop;

    public static CtripOrderCommonFragment getInstance(String type) {
        CtripOrderCommonFragment sf = new CtripOrderCommonFragment();
        sf.TYPE = type;
        return sf;
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

    private void getFirstPageData() {
        page = Constants.PAGE_INDEX;
        getOrdersList();

    }

    /**
     * 订单列表
     */
    private void getOrdersList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getCtripOrderList("xc_order/xc_order_list", TYPE, page, Constants.PAGE_COUNT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CtripOrderListEntity>() {
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
                        } else {
                            page--;
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CtripOrderListEntity orderListEntity) {
                        List<CtripOrderListEntity.OrderList> mData = orderListEntity.list;
                        if (page == Constants.PAGE_INDEX) {
                            if (mData.size() <= 0) {
                                varyViewUtils.showEmptyView();
                            } else {
                                mAdapter.setNewData(processingData(mData));
                                varyViewUtils.showDataView();
                            }
                        } else {
                            if (mData.size() <= 0 || mData.size() < Constants.PAGE_COUNT) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.addData(processingData(mData));
                            }
                        }
                    }
                });
        addSubscription(subscription);
    }

    private ArrayList<MultiItemEntity> processingData(List<CtripOrderListEntity.OrderList> mData) {
        ArrayList<MultiItemEntity> itemDatas = new ArrayList<>();
        for (CtripOrderListEntity.OrderList dataBean : mData) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            String orderDate = sdf.format(dataBean.CreateDateTime * 1000);
            if (!orderDate.equals(currentDate)) {
                currentDate = orderDate;
                itemDatas.add(new CtripOrderListEntity.HeadBean(currentDate));
            }
            itemDatas.add(dataBean);
        }
        return itemDatas;
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
        varyViewUtils.setCtripOrderList(R.id.vary_content, rootView, new View.OnClickListener() {
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
        });

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CtripOrderCommonAdapter();
        mAdapter.bindToRecyclerView(mRecyclerView);

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
                //item点击事件
                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case CtripOrderListLayoutType.content:
                        //商品信息
                        CtripOrderListEntity.OrderList bean = (CtripOrderListEntity.OrderList) adapter.getItem(position);
                        JumpCtripActivityUtils.toCtripOrderDetails(mContext, bean.ResIDValue);
                        break;
                    default:
                        break;

                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case CtripOrderListLayoutType.content:
                        //商品信息
                        CtripOrderListEntity.OrderList bean = (CtripOrderListEntity.OrderList) adapter.getItem(position);
                        switch (view.getId()) {
                            case R.id.tv_share:
                                //分享
                                CtripOrderShareEntity shareEntity = new CtripOrderShareEntity();
                                shareEntity.setId(bean.id);
                                shareEntity.setHotelId(bean.hotelId);
                                shareEntity.setInclusiveAmount(bean.InclusiveAmount);
                                shareEntity.setTimeSpanStart(bean.TimeSpanStart);
                                shareEntity.setTimeSpanEnd(bean.TimeSpanEnd);
                                shareEntity.setNumberOfUnits(bean.NumberOfUnits);
                                shareEntity.setNightNum(bean.nightNum);
                                shareEntity.setHotelName(bean.HotelName);
                                shareEntity.setAddress(bean.Address);
                                shareEntity.setImage(bean.image);
                                shareEntity.setMinPrice(bean.MinPrice);
                                shareEntity.setCtripUserRating(bean.CtripUserRating);
                                JumpCtripActivityUtils.toCtripOrderShare(mContext, shareEntity);
                                break;
                            case R.id.tv_delete:
                                //删除
                                orderDelete(bean.ResIDValue, position);
                                break;
                            case R.id.tv_again:
                                //再次预订
                                JumpCtripActivityUtils.toCtripHotelDetail(mContext, bean.hotelId, null, null);
                                break;
                            case R.id.tv_reminder:
                                //崔确认
                                if (reminderPop == null) {
                                    reminderPop = new CtripWarnCommonPopwindow(getContext());
                                    reminderPop.setContent("已收到您的催单需求，请稍作等待多数订单会在10分钟内确认，请耐心等待。");
                                    reminderPop.setLeftColor("#4289FF");
                                    reminderPop.setLeft("知道了", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            reminderPop.dismiss();
                                        }
                                    });
                                }
                                reminderPop.showPop(mRecyclerView);
                                break;
                            case R.id.tv_submit:
                                //继续提交

                                CtripCommitOrderEntity entity = new CtripCommitOrderEntity();
                                entity.hotelId = bean.hotelId;
                                entity.roomId = bean.RoomID;
                                entity.orderIndex = bean.id;
                                entity.orderPrice = bean.InclusiveAmount;
                                entity.returnBean = bean.ReturnBean;
                                entity.ResID_Value = bean.ResIDValue;
                                entity.hotelName = bean.HotelName;
                                entity.roomType = bean.roomName;
                                entity.bedName = bean.bedName;
                                entity.netMsg = bean.netMsg;
                                entity.breakfast = bean.breakfast;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                entity.checkIn = sdf.format(Long.parseLong(bean.TimeSpanStart) * 1000);
                                entity.checkOut = sdf.format(Long.parseLong(bean.TimeSpanEnd) * 1000);
                                entity.duration = bean.nightNum;
                                entity.number = bean.NumberOfUnits;
                                entity.checkInPerson = bean.users;
                                entity.linkman = bean.contactName;
                                entity.phone = bean.phone;
                                JumpCtripActivityUtils.toCtripOrderPayment(mContext, entity);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
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

    /**
     * 删除订单
     */
    private void orderDelete(String ResID_Value, int position) {
        if (orderDeletePop == null) {
            orderDeletePop = new CtripWarnCommonPopwindow(getContext());
            orderDeletePop.setTitle("删除此订单记录不等于取消预订");
            orderDeletePop.setTitle("删除后订单记录无法还原和查询，确定删除吗？");
            orderDeletePop.setLeft("点错了", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDeletePop.dismiss();
                }
            });
            orderDeletePop.setRight("删除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDeletePop.dismiss();
                    startMyDialog();
                    Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                            ctripDeleteOrder("xc_order/xc_del_order", ResID_Value).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
        }
        orderDeletePop.showPop(mRecyclerView);
    }

    @Subscribe()
    public void receiveEvent(CtripOrderListUpdateModel resultModel) throws PackageManager.NameNotFoundException {
        if (TYPE == OrderType_all) {
            if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_pay) {
                //去支付
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            //改变订单状态为 待发货
                            orderBean.status = "Process";
                            mAdapter.setData(i, orderBean);
                            break;
                        }
                    }

                }
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_receiving) {
                //确认收货
                getFirstPageData();
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            //改变订单状态为 已取消
                            orderBean.status = "Cancel";
                            mAdapter.setData(i, orderBean);
                            break;
                        }
                    }
                }
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            mAdapter.remove(i);
                            break;
                        }
                    }
                }
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_add) {
                //添加新订单
                getFirstPageData();
            }
        } else if (TYPE == OrderType_submit) {
            if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_pay) {
                //去支付
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            mAdapter.remove(i);
                            break;
                        }
                    }
                }
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            //改变订单状态为 已取消
                            orderBean.status = "Cancel";
                            mAdapter.setData(i, orderBean);
                            break;
                        }
                    }
                }
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_add) {
                //添加新订单
                getFirstPageData();
            }
        } else if (TYPE == OrderType_affirm) {
            if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            mAdapter.remove(i);
                            break;
                        }
                    }
                }
            }
        } else if (TYPE == OrderType_expense) {
            if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            mAdapter.remove(i);
                            break;
                        }
                    }
                }
            }
        } else if (TYPE == OrderType_cancel) {
            if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_cancle) {
                //取消订单
                getFirstPageData();
            } else if (resultModel.handleType == CtripOrderListUpdateModel.HandleType_delete) {
                //删除订单
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getItemType() == CtripOrderListLayoutType.content) {
                        CtripOrderListEntity.OrderList orderBean = (CtripOrderListEntity.OrderList) mAdapter.getData().get(i);
                        if (orderBean.ResIDValue.equals(resultModel.ResID_Value)) {
                            mAdapter.remove(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 取消订单
     */
//    private void orderCancel() {
//        if (orderCancelPop == null) {
//            orderCancelPop = new CtripWarnCommonPopwindow(getContext());
//            orderCancelPop.setTitle("确定取消订单吗？");
//            orderCancelPop.setLeft("点错了", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    orderCancelPop.dismiss();
//                }
//            });
//            orderCancelPop.setRight("取消订单", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    JumpCtripActivityUtils.toCtripOrderCancel(mContext);
//                    orderCancelPop.dismiss();
//                }
//            });
//        }
//        orderCancelPop.showPop(mRecyclerView);
//    }
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
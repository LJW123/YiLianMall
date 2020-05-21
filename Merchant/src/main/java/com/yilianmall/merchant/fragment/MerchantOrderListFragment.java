package com.yilianmall.merchant.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.widget.SlidingTabLayout;
import com.yilian.networkingmodule.entity.MerchantOrderListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantExpressSelectActivity;
import com.yilianmall.merchant.activity.MerchantOrderDetailsActivity;
import com.yilianmall.merchant.adapter.OrderListAdapter;
import com.yilianmall.merchant.event.RemoveMerchantManageOrderList;
import com.yilianmall.merchant.event.UpdateMerchantManageOrderList;
import com.yilianmall.merchant.utils.UpdateMerchantOrderNumber;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *@author  Created by LYQ on 2017/9/28.
 * 商家订单列表
 */
public class MerchantOrderListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int orderType, page;
    private OrderListAdapter adapter;
    private SlidingTabLayout slidingTabLayout;
    private int titleCount;
    private View emptyView;
    private View errorView;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.merchant_list_fragment, null, false);
        initView(view);
        initListener();
        return view;
    }

    /**
     * 对应条目进行相关操作后，需要从当前列表移除
     * type是防止移除错乱
     *
     * @param removeMerchantManageOrderList
     */
    @Override
    public void removeMerchantManageOrderList(RemoveMerchantManageOrderList removeMerchantManageOrderList) {
        if (orderType == removeMerchantManageOrderList.type && removeMerchantManageOrderList.position > -1) {
            removeAdapterItem(removeMerchantManageOrderList.position);
        }
    }

    @Override
    public void updateMerchantManageOrderList(UpdateMerchantManageOrderList updateMerchantManageOrderList) {
        Logger.i("发货后这里走了：UpdateMerchantManageOrderList");
        MerchantOrderListEntity.ListBean item = adapter.getItem(updateMerchantManageOrderList.position);
        item.orderStatus = String.valueOf(updateMerchantManageOrderList.orderStatus);
        adapter.notifyItemChanged(updateMerchantManageOrderList.position, item);
    }

    private void initListener() {
        adapter.setOnLoadMoreListener(this, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addSubscribe(UpdateMerchantOrderNumber.getMerchantOrderNumber(mContext));
                getFirstPageData();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                int viewId = view.getId();
                MerchantOrderListEntity.ListBean itemBean = (MerchantOrderListEntity.ListBean) adapter.getItem(position);
                if (itemBean == null) {
                    return;
                }
                final String orderIndex = itemBean.orderIndex;
                if (viewId == R.id.ll_goods_info) {
                    Intent intent = new Intent(mContext, MerchantOrderDetailsActivity.class);
                    intent.putExtra("orderIndex", orderIndex);
                    intent.putExtra("position", position);
                    startActivity(intent);
                } else if (viewId == R.id.btn_order_status) {
                    if (itemBean.orderStatus.equals("2")) {
                        //备货
                        showSystemV7Dialog(null, "确定备货?", "备货", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //备货
                                        orderExport(orderIndex, position);
                                        break;
                                    default:
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                    } else if (itemBean.orderStatus.equals("3") || itemBean.orderStatus.equals("4")) {
                        //整单发货——————>选择快递界面
                        Intent intent = new Intent(mContext, MerchantExpressSelectActivity.class);
                        intent.putExtra("fromPage", "MerchantOrderListFragment");
                        intent.putExtra("parcelIndex", itemBean.goodsIndexStr);
                        intent.putExtra("orderIndex", itemBean.orderIndex);
                        intent.putExtra("position", position);
                        intent.putExtra("orderStatus", itemBean.orderStatus);
                        intent.putExtra(Constants.JUMP_STATUS, "order");
                        startActivity(intent);
                    }
                } else if (viewId == R.id.tv_phone) {
                    PhoneUtil.call(itemBean.orderPhone, mContext);
                }
            }
        });
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    /**
     * 备货
     *
     * @param orderIndex
     * @param position
     */
    private void orderExport(String orderIndex, final int position) {
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .orderExport(orderIndex, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        showToast("备货成功");
                                        addSubscribe(UpdateMerchantOrderNumber.getMerchantOrderNumber(mContext));
                                        removeAdapterItem(position);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    /**
     * 移除item某个条目
     *
     * @param position
     */
    private void removeAdapterItem(int position) {
        if (adapter != null) {
            int lastCount = adapter.getItemCount();
            if (lastCount > 0) {
                adapter.remove(position);
//                此处需要重新获取adapter条目数量
                int afterCount = adapter.getItemCount();
                if (afterCount <= 0) {
                    adapter.setEmptyView(getEmptyView());
                }
            }
        }
    }

    private void refreshAdapterItem(int position, int status) {
        MerchantOrderListEntity.ListBean item = adapter.getItem(position);
        item.orderStatus = String.valueOf(status);
        adapter.notifyItemChanged(position);
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new OrderListAdapter(R.layout.merchant_item_order_list);
        recyclerView.setAdapter(adapter);
    }

    private boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst && isVisibleToUser) {
            isFirst = !isFirst;
            Bundle arguments = getArguments();
            orderType = arguments.getInt("type");
            slidingTabLayout = (SlidingTabLayout) arguments.getSerializable("SlidingTabLayout");
            titleCount = arguments.getInt("titleCount");
            page = 0;
            initData();
        }
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(200);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            getFirstPageData();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getData() {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantCenterOrderList(orderType, page, new Callback<MerchantOrderListEntity>() {
                    @Override
                    public void onResponse(Call<MerchantOrderListEntity> call, Response<MerchantOrderListEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        List<MerchantOrderListEntity.ListBean> list = response.body().list;
                                        if (null != list && list.size() > 0) {
                                            if (page > 0) {
                                                adapter.addData(list);
                                            } else {
                                                adapter.setNewData(list);
                                            }
                                            if (list.size() < Constants.PAGE_COUNT) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
                                                adapter.setNewData(list);//从有数据变为无数据时，不显示空布局的问题
                                                adapter.setEmptyView(getEmptyView());
                                            }
                                        }

                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<MerchantOrderListEntity> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (page == 0) {
                            adapter.setEmptyView(getErrorView());
                        }
                    }
                });
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getData();
    }

    public View getEmptyView() {
        if (null == emptyView) {
            emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
        }
        return emptyView;
    }

    public View getErrorView() {
        if (null == errorView) {
            errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
            TextView tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
            tvRefresh.setOnClickListener(this);
        }
        return errorView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_refresh) {
            getFirstPageData();
        }
    }
}

package com.yilian.mall.ui.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.OrderNewListAdapter;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.OrderNewListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 * 消息中心订单列表
 */

public class OrderListFragment extends InformationFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private OrderNewListAdapter orderAdapter;

    @Override
    public void getNewData() {
        /**
         * 防止第一次走了失败不走onListener方法
         */
        if (null == orderAdapter) {
            orderAdapter = new OrderNewListAdapter(R.layout.item_infomation);
            recyclerView.setAdapter(orderAdapter);
            onListener();
        }
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getOrderNewList(String.valueOf(page), new Callback<OrderNewListEntity>() {
                    @Override
                    public void onResponse(Call<OrderNewListEntity> call, Response<OrderNewListEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            HttpResultBean bean = response.body();
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        List<OrderNewListEntity.DataBean> data = response.body().data;
                                        initList(data);
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<OrderNewListEntity> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        orderAdapter.setEmptyView(getErrorView());
                    }
                });
    }

    private void onListener() {
        orderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //同公用的跳转
                OrderNewListEntity.DataBean item= (OrderNewListEntity.DataBean) adapter.getItem(position);
                if (null!=item)
                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(item.type, item.data);
            }
        });
        orderAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    private void initList(List<OrderNewListEntity.DataBean> data) {
        if (null != data && data.size() > 0) {
            if (page > 0) {
                orderAdapter.addData(data);
            } else {
                orderAdapter.setNewData(data);
            }
            if (data.size() < Constants.PAGE_COUNT) {
                orderAdapter.loadMoreEnd();
            } else {
                orderAdapter.loadMoreComplete();
            }
        } else {
            if (page == 0) {
                orderAdapter.setEmptyView(getEmptyView());
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getNewData();
    }
}

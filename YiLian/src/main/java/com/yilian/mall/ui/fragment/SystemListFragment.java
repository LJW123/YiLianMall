package com.yilian.mall.ui.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.adapter.SystemNewListAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.SystemNewListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/9/5 0005.
 * 系统消息
 */

public class SystemListFragment extends InformationFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private SystemNewListAdapter systemAdapter;

    @Override
    public void getNewData() {
        if (null==systemAdapter){
            systemAdapter = new SystemNewListAdapter(R.layout.item_infomation);
            onListener();
            recyclerView.setAdapter(systemAdapter);
        }

        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSystemNewList(String.valueOf(page), new Callback<SystemNewListEntity>() {
                    @Override
                    public void onResponse(Call<SystemNewListEntity> call, Response<SystemNewListEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        List<SystemNewListEntity.DataBean> data = response.body().data;
                                        initList(data);
                                        break;
                                }
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<SystemNewListEntity> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        systemAdapter.setEmptyView(getErrorView());
                    }
                });
    }

    private void onListener() {
        systemAdapter.setOnLoadMoreListener(this,recyclerView);
    }

    private void initList(List<SystemNewListEntity.DataBean> data) {
        if (null != data && data.size() > 0) {
            if (page > 0) {
                systemAdapter.addData(data);
            } else {
                systemAdapter.setNewData(data);
            }
            if (data.size()< Constants.PAGE_COUNT){
                systemAdapter.loadMoreEnd();
            }else {
                systemAdapter.loadMoreComplete();
            }
        } else {
            if (page==0){
                systemAdapter.setEmptyView(getEmptyView());
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getNewData();
    }
}

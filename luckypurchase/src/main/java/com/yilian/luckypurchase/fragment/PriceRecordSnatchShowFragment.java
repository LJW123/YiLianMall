package com.yilian.luckypurchase.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyUnboxingActivity;
import com.yilian.luckypurchase.adapter.SnatchShowAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.SnatchShowListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Created by LYQ on 2017/10/30.
 * 晒单记录
 */

public class PriceRecordSnatchShowFragment extends BaseMyRecordFragment {

    private SnatchShowAdapter adapter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    public void getData() {
        if (null==adapter){
            adapter = new SnatchShowAdapter(R.layout.lucky_item_record_show_list);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(this, recyclerView);
            initAdapterListener();
        }
        /**
         * 我的晒单列表固定传值
         * type 0我的夺宝晒单记录
         * snatchIndex 0所有
         */
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getSnatchShowList("1", page, "0", new Callback<SnatchShowListEntity>() {
                    @Override
                    public void onResponse(Call<SnatchShowListEntity> call, Response<SnatchShowListEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        List<SnatchShowListEntity.SnatchInfoBean> showSnatchInfo = response.body().snatchInfo;
                                        if (page > 0) {
                                            if (null==showSnatchInfo||showSnatchInfo.size()<=0){
                                                adapter.loadMoreEnd();
                                            }
                                            adapter.addData(showSnatchInfo);
                                        } else {
                                            if (null != showSnatchInfo && showSnatchInfo.size() > 0) {
                                                adapter.setNewData(showSnatchInfo);
                                            } else {
                                                adapter.setEmptyView(getEmptyView());
                                            }
                                            if (Constants.PAGE_COUNT>showSnatchInfo.size()){
                                                adapter.loadMoreEnd();
                                            }else {
                                                adapter.loadMoreComplete();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<SnatchShowListEntity> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        if (page>0){
                            showToast(R.string.aliwx_net_null_setting);
                        }else {
                            adapter.setEmptyView(getErrorView());
                        }
                    }
                });
    }

    public void initAdapterListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SnatchShowListEntity.SnatchInfoBean bean = (SnatchShowListEntity.SnatchInfoBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, LuckyUnboxingActivity.class);
                intent.putExtra("activity_id", bean.commentIndex);
                startActivity(intent);
            }
        });
    }

}

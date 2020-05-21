package com.yilian.luckypurchase.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyActivityDetailActivity;
import com.yilian.luckypurchase.activity.LuckyNumberRecordActivity;
import com.yilian.luckypurchase.adapter.MyRecordListAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.SnatchAwardListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Created by LYQ on 2017/10/30.
 * 已揭晓
 */

public class PriceRecordJoinListFragment extends BaseMyRecordFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private MyRecordListAdapter adapter;
    private String type;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    public void getData() {
        init();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .snatchJoinList(type, userId, page, new Callback<SnatchAwardListEntity>() {
                    @Override
                    public void onResponse(Call<SnatchAwardListEntity> call, Response<SnatchAwardListEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        //根据页面去判断当前应该显示的数据
                                        List<SnatchAwardListEntity.SnatchInfoBean> snatchInfo = response.body().snatchInfo;
                                        if (page > 0) {
                                            if (null == snatchInfo && snatchInfo.size() <= 0) {
                                                adapter.loadMoreEnd();
                                            }
                                            adapter.addData(snatchInfo);
                                        } else {
                                            if (null != snatchInfo && snatchInfo.size() > 0) {

                                                adapter.setNewData(snatchInfo);
                                            } else {
                                                adapter.setEmptyView(getEmptyView());
                                            }
                                            if (Constants.PAGE_COUNT > snatchInfo.size()) {
                                                adapter.loadMoreEnd();//加载结束
                                            } else {
                                                adapter.loadMoreComplete();//加载完成有下一页数据
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
                    public void onFailure(Call<SnatchAwardListEntity> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        if (page > 0) {
                            showToast(R.string.aliwx_net_null_setting);
                        } else {
                            adapter.setEmptyView(getErrorView());
                        }
                    }
                });

    }

    /**
     * 处理请求的参数
     */
    private void init() {
        if (null == adapter) {
            adapter = new MyRecordListAdapter(null);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(this,recyclerView);
            initAdapterListener();
        }
        switch (position){
            case 0:
                //进行中
                type="1";
                break;
            case 1:
                //已揭晓
                type="2";
                break;
            default:
                break;
        }
    }

    private void initAdapterListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SnatchAwardListEntity.SnatchInfoBean item= (SnatchAwardListEntity.SnatchInfoBean) adapter.getItem(position);

                int i = view.getId();
                if (i == R.id.tv_look_detail) {
                    Intent intent = new Intent(mContext, LuckyNumberRecordActivity.class);
                    intent.putExtra("activity_id", item.activityId);
                    intent.putExtra("from", "list");
                    startActivity(intent);
                } else if (i == R.id.tv_add) {
                    //跳转详情并弹出popupWindow
                    Intent intent=new Intent(mContext, LuckyActivityDetailActivity.class);
                    intent.putExtra("activity_id",item.activityId);
                    intent.putExtra("type", "0");
                    intent.putExtra("show_window","1");
                    startActivity(intent);
                } else if (i == R.id.ll_content) {
                    //跳转详情不弹窗
                    Intent intent=new Intent(mContext, LuckyActivityDetailActivity.class);
                    intent.putExtra("activity_id",item.activityId);
                    intent.putExtra("type","0");
                    intent.putExtra("show_window","0");
                    startActivity(intent);
                }
            }
        });
    }

}
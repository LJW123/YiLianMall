package com.yilian.mall.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MyGroupsListAdapter;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.networkingmodule.entity.MyGroupsListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2017/5/19 0019.
 * 已中奖
 */
public class MySpellGroupWinPrizeFragment extends BaseFragment  {
    public PullToRefreshListView listView;
    public ImageView ivNothing;
    public LinearLayout llError;
    public TextView tvRefresh;
    public String type;
    public Context mCntext;
    private int page;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_spell_group, container, false);
        listView = (PullToRefreshListView) view.findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);
        llError = (LinearLayout) view.findViewById(R.id.ll_error_view);
        llError.setVisibility(View.GONE);
        tvRefresh = (TextView) view.findViewById(R.id.tv_update_error);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        mCntext = getActivity();
        page = 0;
        loadData();
        initListener();
        return view;
    }

    private void initListener() {
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page=0;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                loadData();
            }
        });
    }

    private MyGroupsListAdapter adapter;
    private ArrayList<MyGroupsListEntity.DataBean> myGroupList = new ArrayList<>();

    @Override
    public void loadData() {
        type = "3";
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMySpellGroupsListData(type, String.valueOf(page), new Callback<MyGroupsListEntity>() {
                    @Override
                    public void onResponse(Call<MyGroupsListEntity> call, Response<MyGroupsListEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        llError.setVisibility(View.GONE);
                                        ivNothing.setVisibility(View.GONE);
                                        listView.setVisibility(View.VISIBLE);
                                        List<MyGroupsListEntity.DataBean> groupList = response.body().data;
                                        if (null != groupList && groupList.size() > 0) {
                                            if (page > 0) {
                                                myGroupList.addAll(groupList);
                                            } else {
                                                myGroupList.clear();
                                                myGroupList.addAll(groupList);
                                                adapter = new MyGroupsListAdapter(groupList);
                                                listView.setAdapter(adapter);

                                            }
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            if (page > 0) {
                                                showToast("暂无更多");
                                            } else {
                                                listView.setVisibility(View.GONE);
                                                ivNothing.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        listView.onRefreshComplete();
                                        stopMyDialog();
                                        break;
                                    default:
                                        Logger.i(getClass().getSimpleName() + response.body().code);
                                        stopMyDialog();
                                        listView.onRefreshComplete();

                                        break;
                                }
                            } else {
                                listView.setVisibility(View.GONE);
                                ivNothing.setVisibility(View.VISIBLE);
                                listView.onRefreshComplete();
                                stopMyDialog();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyGroupsListEntity> call, Throwable t) {
                        stopMyDialog();
                        listView.setVisibility(View.GONE);
                        llError.setVisibility(View.VISIBLE);
                        listView.onRefreshComplete();

                    }
                });

    }

}

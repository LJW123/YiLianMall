package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.FavoriteMerchantRecycleAdapter;
import com.yilian.mall.adapter.decoration.DividerItemDecoration;
import com.yilian.mall.ui.MTMerchantDetailActivity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.FavoriteEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liuyuqi on 2016/10/22 0022.
 * 收藏-本地商家
 */

public class FavoriteMerchantFragment2 extends BaseFragment {

    StringBuilder sbCollectIds = new StringBuilder();
    List<String> checkCollectIds = new ArrayList<>();
    List<Boolean> isAllSelectFlags = new ArrayList<>();

    private LinearLayout ll_select;
    private boolean isCompile;
    private CheckBox tv_select;
    private ImageView ivNothing;
    // 用来存所有 checkbox 的选中状态
    private Map<Integer, Boolean> flags = new HashMap<>();
    private MySwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private Button btnCancel;
    private String type;
    private ArrayList<FavoriteEntity.ListBean> favoriteList = new ArrayList<>();
    private FavoriteMerchantRecycleAdapter adapter1;
    private boolean hasRefreshData = false;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_favorite_fragment2, container, false);
        ivNothing = (ImageView) view.findViewById(R.id.iv_nothing);
        refreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_red));
        refreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL, 1, R.color.color_lines);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (null == adapter1) {
            adapter1 = new FavoriteMerchantRecycleAdapter(R.layout.item_favorite_merchant2, favoriteList);
        }
        initHeadView();
        recyclerView.setAdapter(adapter1);
        ll_select = (LinearLayout) view.findViewById(R.id.ll_select);
        tv_select = (CheckBox) view.findViewById(R.id.tv_all_select);
        btnCancel = (Button) view.findViewById(R.id.cancel);


        initListener();
        return view;
    }

    boolean isFirst = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isFirst) {
            isFirst = !isFirst;
            Logger.i("isFirst=" + isFirst);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (refreshLayout != null) {
                                    refreshLayout.setRefreshing(true);
                                }
                                isFirst = false;
                                initData();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void initData() {
        type = "2";
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getFavoriteData(type, new Callback<FavoriteEntity>() {
                    @Override
                    public void onResponse(Call<FavoriteEntity> call, Response<FavoriteEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        ArrayList<FavoriteEntity.ListBean> list = (ArrayList<FavoriteEntity.ListBean>) response.body().list;
                                        if (null != list && list.size() > 0) {
                                            refreshLayout.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            ivNothing.setVisibility(View.GONE);
                                            favoriteList.clear();
                                            for (int i = 0; i < list.size(); i++) {
                                                list.get(i).isShowCheck = isCompile;
                                                flags.put(i, false);
                                            }
                                            favoriteList.addAll(list);
                                            adapter1.setNewData(favoriteList);
                                        } else {
                                            refreshLayout.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.GONE);
                                            ivNothing.setVisibility(View.VISIBLE);
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
                    public void onFailure(Call<FavoriteEntity> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initHeadView() {
        View view = View.inflate(mContext, R.layout.item_favorite_list_head, null);
        adapter1.addHeaderView(view);
    }

    @Override
    protected void loadData() {
    }


    private void initListener() {

        refreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });


        adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("0".equals(adapter1.getItem(position).status)) {
                    showToast("此商家已下架");
                    return;
                }
                //跳转商家详情
                Intent intent = new Intent(getActivity(), MTMerchantDetailActivity.class);
                intent.putExtra("merchant_id", adapter1.getItem(position).collectId);
                Logger.i("merchant_id-" + adapter1.getItem(position).collectId);
                startActivity(intent);
            }
        });
        adapter1.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.cb_select:
                        flags.put(position, ((CheckBox) view).isChecked());
                        favoriteList.get(position).isCheck = ((CheckBox) view).isChecked();
                        bianli();
                        break;
                }
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //全选
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    for (int i = 0; i < favoriteList.size(); i++) {
                        flags.put(i, true);
                        favoriteList.get(i).isCheck = true;
                    }
                } else {
                    for (int i = 0; i < favoriteList.size(); i++) {
                        flags.put(i, false);
                        favoriteList.get(i).isCheck = false;
                    }
                }
                adapter1.notifyDataSetChanged();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void cancel() {
        sbCollectIds.delete(0, sbCollectIds.length());
        checkCollectIds.clear();

        for (int i = 0; i < favoriteList.size(); i++) {
            if (flags.get(i)) {
                checkCollectIds.add(favoriteList.get(i).collectIndex);
            }
        }

        for (int i = 0; i < checkCollectIds.size(); i++) {
            if (i == checkCollectIds.size() - 1) {
                sbCollectIds.append(checkCollectIds.get(i));
            } else {
                sbCollectIds.append(checkCollectIds.get(i) + ",");
            }
        }
        String sbCollectIdsStr = sbCollectIds.toString();
        if (sbCollectIdsStr.length() <= 0 && favoriteList.size() > 0) {
            showToast("请选择需要取消的本地商家");
            return;
        } else if (sbCollectIdsStr.length() <= 0 && favoriteList.size() <= 0) {
            showToast("亲，没有可取消的本地商家哦");
            return;
        }

        initCancelData(sbCollectIdsStr);
    }

    private void initCancelData(String sbCollectIdsStr) {
        startMyDialog();

        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .cancelFavorite(sbCollectIdsStr, new Callback<BaseEntity>() {
                    @Override
                    public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast("取消收藏成功");
                                        sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                        initData();//刷新数据
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<BaseEntity> call, Throwable t) {
                        showToast("取消收藏失败");
                        stopMyDialog();
                    }
                });
    }

    public void initActivirtyState(boolean isChecked) {
        this.isCompile = isChecked;

        if (isCompile) {
            ll_select.setVisibility(View.VISIBLE);
        } else if (!isCompile) {
            ll_select.setVisibility(View.GONE);
            tv_select.setChecked(false);
        } else {
            isCompile = false;
        }

        for (int i = 0; i < favoriteList.size(); i++) {
            favoriteList.get(i).isShowCheck = isCompile;
        }
        adapter1.notifyDataSetChanged();
    }

    private void bianli() {
        isAllSelectFlags.clear();
        for (int i = 0; i < favoriteList.size(); i++) {
            isAllSelectFlags.add(flags.get(i));
        }
        if (isAllSelectFlags.contains(false)) {
            tv_select.setChecked(false);
        } else {
            tv_select.setChecked(true);
        }
    }

}

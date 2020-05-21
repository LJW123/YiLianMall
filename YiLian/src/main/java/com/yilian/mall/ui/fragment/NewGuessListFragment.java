package com.yilian.mall.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.NewGuessListAdapter;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.GuessListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/10/13 0013.
 */

public class NewGuessListFragment extends JPBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private View rootView, emptyView, errorView;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private int page = 0;
    private NewGuessListAdapter adapter;
    private ArrayList<GuessListEntity.ListBean> list = new ArrayList<>();

    //分类:进行中 已开奖 我参与的
    private String category;
    //页面第一次加载标识
    private boolean isFirst = true;
    //数据第一次加载标识
    private boolean getFirstPageDataFlag = true;
    private TextView tvRefresh, tvNoData;
    //
    private boolean isRefresh = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        category = getArguments().getString("category");

        if (isVisibleToUser && isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (swipeRefreshLayout != null) {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                    isFirst = false;
                                    getFirstPageData();
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_new_guess_list, container, false);
        }
        ViewUtils.inject(this, rootView);
        initView();
        initListener();
        return rootView;
    }

    private void initView() {
        emptyView = View.inflate(mContext, R.layout.merchant_barcode_nothing, null);
        tvNoData = (TextView) emptyView.findViewById(R.id.tv_no_data);
        tvNoData.setText("暂无更多数据");
        errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
        tvRefresh = (TextView) errorView.findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeRefreshLayout.setRefreshing(true);
                getFirstPageData();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            adapter = new NewGuessListAdapter(R.layout.item_new_guess_list, category);
        }
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (category) {
            case "1":
                isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_GUESS_GOING_ONE, mContext, false);
                break;
            case "2":
                isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_GUESS_GOING_TWO, mContext, false);
                break;
            case "3":
                isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_GUESS_GOING_THREE, mContext, false);
                break;
            default:
                break;
        }


        if (isRefresh) {
            swipeRefreshLayout.setRefreshing(true);
            getFirstPageDataFlag = true;
            getFirstPageData();

            isRefresh = false;
            switch (category) {
                case "1":
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_GOING_ONE, isRefresh, mContext);
                    break;
                case "2":
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_GOING_TWO, isRefresh, mContext);
                    break;
                case "3":
                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_GUESS_GOING_THREE, isRefresh, mContext);
                    break;
            }
            Logger.i("ray-" + "走了这里22");
        }

    }

    private void initListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                if (scrollDy > ScreenUtils.getScreenHeight(mContext) * 3) {
                    iv_return_top.setVisibility(View.VISIBLE);
                } else {
                    iv_return_top.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        iv_return_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        adapter.setOnLoadMoreListener(this, recyclerView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageDataFlag = true;
                getFirstPageData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    @Override
    protected void loadData() {
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void getData() {
        if ("1".equals(category) || "2".equals(category)) {
            RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .guessList(category, String.valueOf(page), "20", new Callback<GuessListEntity>() {
                        @Override
                        public void onResponse(Call<GuessListEntity> call, Response<GuessListEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    GuessListEntity entity = response.body();
                                    switch (entity.code) {
                                        case 1:
                                            ArrayList<GuessListEntity.ListBean> newList = entity.list;
                                            if (newList.size() > 0 && newList != null) {
                                                if (getFirstPageDataFlag) {
                                                    adapter.setNewData(newList);
                                                    getFirstPageDataFlag = false;
                                                    list.clear();
                                                } else {
                                                    adapter.addData(newList);
                                                }
                                                list.addAll(newList);

                                                if (newList.size() < Constants.PAGE_COUNT_20) {
                                                    adapter.loadMoreEnd();
                                                } else {
                                                    adapter.loadMoreComplete();
                                                }
                                            } else {
                                                if (page == 0) {
                                                    adapter.setNewData(newList);
                                                    adapter.setEmptyView(emptyView);
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                            netRequestEnd();
                        }

                        @Override
                        public void onFailure(Call<GuessListEntity> call, Throwable t) {
                            if (page == 0) {
                                adapter.setEmptyView(errorView);
                            } else if (page > 0) {
                                page--;
                            }
                            adapter.loadMoreFail();
                            netRequestEnd();
                            showToast(R.string.merchant_module_service_exception);
                        }
                    });
        } else if ("3".equals(category)) {
            RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .guessListSelf(String.valueOf(page), "20", new Callback<GuessListEntity>() {
                        @Override
                        public void onResponse(Call<GuessListEntity> call, Response<GuessListEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    GuessListEntity entity = response.body();
                                    switch (entity.code) {
                                        case 1:
                                            ArrayList<GuessListEntity.ListBean> newList = entity.list;
                                            if (newList.size() > 0 && newList != null) {
                                                if (getFirstPageDataFlag) {
                                                    adapter.setNewData(newList);
                                                    getFirstPageDataFlag = false;
                                                    list.clear();
                                                } else {
                                                    adapter.addData(newList);
                                                }
                                                list.addAll(newList);

                                                if (newList.size() < Constants.PAGE_COUNT_20) {
                                                    adapter.loadMoreEnd();
                                                } else {
                                                    adapter.loadMoreComplete();
                                                }
                                            } else {
                                                if (page == 0) {
                                                    adapter.setNewData(newList);
                                                    adapter.setEmptyView(emptyView);
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                            netRequestEnd();
                        }

                        @Override
                        public void onFailure(Call<GuessListEntity> call, Throwable t) {
                            if (page == 0) {
                                adapter.setEmptyView(errorView);
                            } else if (page > 0) {
                                page--;
                            }
                            adapter.loadMoreFail();
                            netRequestEnd();
                            showToast(R.string.merchant_module_service_exception);
                        }
                    });

        }
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }


    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        getNextPageData();
    }
}

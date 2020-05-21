package com.yilian.mall.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.yilian.mall.adapter.MallListAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.GoodsListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ray_L_Pain on 2017/7/20 0020.
 */

public class MallFragment extends JPBaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private View rootView;
    @ViewInject(R.id.iv_return_top)
    ImageView iv_return_top;

    ////
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private int page = 0;
    private MallListAdapter adapter;
    private ArrayList<com.yilian.networkingmodule.entity.JPGoodsEntity> list = new ArrayList<>();

    //分类 --- 易划算 或 奖券购
    public String category;
    //分类id
    public String jpLevel1CategoryId;
    //第一次加载的标识
    private boolean isFirst = true;
    private View emptyView, errorView;
    private TextView tvRefresh, tvNoData;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        category = getArguments().getString("category");
        jpLevel1CategoryId = getArguments().getString("categoryId");
        if (jpLevel1CategoryId == null) {
            jpLevel1CategoryId = "0";
        }

        Logger.i("isFirst=" + isFirst);
        if (isVisibleToUser && isFirst) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (null != getActivity()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (swipeRefreshLayout != null) {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                    isFirst = false;
                                    Logger.i("走了获取数据这里1");
                                    getData();
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
            rootView = inflater.inflate(R.layout.fragment_mall, container, false);
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
                Logger.i("走了获取数据这里2");
                getData();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        adapter = new MallListAdapter(list, category);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
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
                page = 0;
                getFirstPageDataFlag = true;
                Logger.i("走了获取数据这里3");
                getData();
                adapter.setEnableLoadMore(false);
            }
        });
    }

    /**
     * 网络请求结束，各个控件恢复初始状态
     */
    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    private boolean getFirstPageDataFlag = true;

    public void getData() {
        switch (category) {
            case "1":
                RetrofitUtils.getInstance(mContext).getYhsList(String.valueOf(page), "30", jpLevel1CategoryId, new Callback<GoodsListEntity>() {
                    @Override
                    public void onResponse(Call<GoodsListEntity> call, Response<GoodsListEntity> response) {
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        GoodsListEntity goodsBody = response.body();
                                        ArrayList<com.yilian.networkingmodule.entity.JPGoodsEntity> newList = goodsBody.data.goods;
                                        Logger.i("走了获取数据这里" + newList.size());
                                        if (newList.size() > 0) {
                                            if (getFirstPageDataFlag) {
                                                adapter.setNewData(newList);
                                                getFirstPageDataFlag = false;
                                                list.clear();
                                            } else {
                                                adapter.addData(newList);
                                            }
                                            list.addAll(newList);

                                            if (newList.size() < Constants.PAGE_COUNT) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
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
                    public void onFailure(Call<GoodsListEntity> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.net_work_not_available);
                    }
                });
                break;

            case "2":
                RetrofitUtils.getInstance(mContext).getJfgList(String.valueOf(page), "30", jpLevel1CategoryId, new Callback<GoodsListEntity>() {
                    @Override
                    public void onResponse(Call<GoodsListEntity> call, Response<GoodsListEntity> response) {
                        GoodsListEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        ArrayList<com.yilian.networkingmodule.entity.JPGoodsEntity> newList = body.data.goods;
                                        if (newList.size() > 0) {
                                            if (getFirstPageDataFlag) {
                                                adapter.setNewData(newList);
                                                getFirstPageDataFlag = false;
                                                list.clear();
                                            } else {
                                                adapter.addData(newList);
                                            }
                                            list.addAll(newList);

                                            if (newList.size() < Constants.PAGE_COUNT) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        } else {
                                            if (page == 0) {
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
                    public void onFailure(Call<GoodsListEntity> call, Throwable t) {
                        if (page == 0) {
                            adapter.setEmptyView(errorView);
                        } else if (page > 0) {
                            page--;
                        }
                        adapter.loadMoreFail();
                        netRequestEnd();
                        showToast(R.string.net_work_not_available);
                    }
                });
                break;
        }
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onLoadMoreRequested() {
        swipeRefreshLayout.setEnabled(false);
        page++;
        Logger.i("走了获取数据这里4");
        getData();
    }
}

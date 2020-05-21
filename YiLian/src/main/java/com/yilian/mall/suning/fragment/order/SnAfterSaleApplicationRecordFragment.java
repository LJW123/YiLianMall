package com.yilian.mall.suning.fragment.order;

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
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.suning.activity.SnHomePageActivity;
import com.yilian.mall.suning.adapter.SnAfterSaleApplicationRecordAdapter;
import com.yilian.mall.suning.utils.JumpSnActivityUtils;
import com.yilian.mall.ui.fragment.JPBaseFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.suning.SnAfterSaleApplicationRecordListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 申请记录
 *
 * @author Created by Zg on 2018/7/31.
 */
public class SnAfterSaleApplicationRecordFragment extends JPBaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    private VaryViewUtils varyViewUtils;

    private int page = Constants.PAGE_INDEX;//页数

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SnAfterSaleApplicationRecordAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.jd_fragment_order_common, null);
        }
        initView();
        initListener();
        return rootView;
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(mContext);
        varyViewUtils.setVaryViewBySnOrderList(R.id.vary_content, rootView, new RefreshClickListener(), "没有申请记录哦", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpSnActivityUtils.toSnHomePageActivity(mContext, SnHomePageActivity.TAB_HOME, null);
            }
        });

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SnAfterSaleApplicationRecordAdapter();
        mRecyclerView.setAdapter(mAdapter);

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
                //售后详情
                SnAfterSaleApplicationRecordListEntity.DataBean mData = (SnAfterSaleApplicationRecordListEntity.DataBean) adapter.getItem(position);
                JumpSnActivityUtils.toSnAfterSaleDetails(mContext, mData.getId());
            }
        });

    }

    private void getFirstPageData() {
        getOrdersList();
    }

    /**
     * 列表
     */
    private void getOrdersList() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getSnAfterSaleApplicationRecordList("suning_aftersale/suning_aftersale_record", page, Constants.PAGE_COUNT, "").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnAfterSaleApplicationRecordListEntity>() {
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
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(SnAfterSaleApplicationRecordListEntity entity) {
                        List<SnAfterSaleApplicationRecordListEntity.DataBean> data = entity.data;
                        if (page == Constants.PAGE_INDEX) {
                            if (data.size() <= 0) {
                                varyViewUtils.showEmptyView();
                            } else {
                                mAdapter.setNewData(data);
                                varyViewUtils.showDataView();
                            }
                        } else {
                            if (data.size() <= 0 || data.size() < Constants.PAGE_COUNT) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.addData(data);
                            }
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    protected void loadData() {
        if (varyViewUtils != null) {
            varyViewUtils.showLoadingView();
        }
        getFirstPageData();
    }

    @Override
    public void onLoadMoreRequested() {
        Logger.i("getShopsList：onLoadMoreRequested");
        getNextPageData();
        swipeRefreshLayout.setEnabled(false);
    }    //按钮的点击事件

    private void getNextPageData() {
        page++;
        getOrdersList();
    }

    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrdersList();
                }
            }, 1000);
        }
    }    @Override
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
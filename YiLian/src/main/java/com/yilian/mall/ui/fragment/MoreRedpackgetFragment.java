package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;

import com.yilian.mall.adapter.MorePackgetsAdapter;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Ip;
import com.yilian.networkingmodule.entity.StealMoreRedpackgs;

import com.yilian.mall.widgets.MySwipeRefreshLayout;
import com.yilian.mylibrary.Constants;

import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ZYH on 2017/8/1 0001.
 * 抢更多奖励的fragment
 */
public class MoreRedpackgetFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private MySwipeRefreshLayout refreshLayout;
    private MorePackgetsAdapter moreRedPackgeAdapter;
    public List<StealMoreRedpackgs.RedPackgeDetails> list;
    private int page = 0;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steal_redpackget_list, container, false);
        initView(view);
        initListener();
        return view;
    }


    private void initView(View view) {

        list = new ArrayList<>();

        refreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        refreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);

        refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.pull_listView);


        moreRedPackgeAdapter = new MorePackgetsAdapter(R.layout.item_more_redpackget, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(moreRedPackgeAdapter);
    }


    private void initData() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
        getFirstPageData();
        Logger.i("ray-走了这里拉拉2");
    }

    boolean isFirst = true;

    //首次进入时进行刷新操作
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

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
                                    isFirst = false;
                                    initData();
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

    @SuppressWarnings("unchecked")
    private void getData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getMoreRedPackgetsResult("stealbonus/merchant_list", page
                , PreferenceUtils.readStrConfig(Constants.SPKEY_LOCATION_CITY_ID, mContext)
                , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, mContext)
                , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, mContext)
                , Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StealMoreRedpackgs>() {
                    @Override
                    public void onCompleted() {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        refreshLayout.setRefreshing(false);
                        moreRedPackgeAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(StealMoreRedpackgs getMyRedpackgets) {
                        setData(getMyRedpackgets);
                    }

                });
        addSubscription(subscription);

    }

    private void setData(StealMoreRedpackgs getMyRedpackgets) {
        showToast(getMyRedpackgets.msg);
        List<StealMoreRedpackgs.RedPackgeDetails> data = getMyRedpackgets.data;
        if (page <= 0) {
            if (null == data || data.size() <= 0) {
                moreRedPackgeAdapter.loadMoreEnd();
                //无数据 TODO 放置无数据页面
                moreRedPackgeAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
            } else {
                moreRedPackgeAdapter.setNewData(data);//添加新的数据
                moreRedPackgeAdapter.loadMoreComplete();
            }
        } else {
            if (data.size() >= Constants.PAGE_COUNT) {//每页30条数据
                moreRedPackgeAdapter.loadMoreComplete();//加载完成，上拉可以加更多
            } else {
                moreRedPackgeAdapter.loadMoreEnd();//没有更多的数据
            }
            moreRedPackgeAdapter.addData(data);
        }
    }


    @Override
    protected void loadData() {
    }

    private void initListener() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });

        //上拉加载更多监听
        moreRedPackgeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, mRecyclerView);
        moreRedPackgeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StealMoreRedpackgs.RedPackgeDetails details= (StealMoreRedpackgs.RedPackgeDetails) adapter.getItem(position);
                if (details!=null){
                    Intent intent=new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("url", Ip.getWechatURL(mContext)+"m/activity/stealPackets/steaIndex1.php?MerID="+details.merchantId);
                    startActivity(intent);
                }
            }
        });


    }

    private void getNextPageData() {
        page++;
        getData();
    }

    private void getFirstPageData() {
        page = 0;
        getData();
    }
}

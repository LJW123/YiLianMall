package com.yilianmall.merchant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.widget.MySwipeRefreshLayout;
import com.yilian.networkingmodule.entity.SendRedTotals;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.SendPackgeRecorderAdapter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * create  ZYH  2017/12/6
 * @author ZYH
 */
public class MerchantSendPackgeRecorderActivity extends BaseSimpleActivity implements View.OnClickListener {
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SendPackgeRecorderAdapter mAdapter;
    private int page = 0;
    private TextView v3Titile;
    private ImageView v3Back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_packge_recorder);
        initView();

        initLisner();
    }

    private void initLisner() {
        v3Back.setOnClickListener(this);
        mySwipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPagerData();
            }
        });

        //上拉加载更多监听
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPagerData();
            }
        }, mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, MerchantThrowedRecorderActivity.class);
                intent.putExtra("id", ((SendRedTotals.Data) adapter.getItem(position)).id);//过期奖励
                startActivity(new Intent(mContext, MerchantThrowedRecorderActivity.class));

            }
        });

    }

    private void initView() {
        v3Titile = (TextView) findViewById(R.id.v3Title);
        v3Titile.setText("奖励总额");
        v3Titile.setTextColor(getResources().getColor(R.color.merchant_black));
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setVisibility(View.VISIBLE);

        mySwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.merchant_swipe_refresh);
        mySwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mySwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.merchant_color_red));


        mRecyclerView = (RecyclerView) findViewById(R.id.merchant_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new SendPackgeRecorderAdapter(R.layout.merchant_item_send_packge_recorder, false);
        mRecyclerView.setAdapter(mAdapter);

    }

    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst && hasFocus) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (null != mContext) {
                            runOnUiThread(new Runnable() {
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
    }

    private void initData() {
        if (mySwipeRefreshLayout != null) {
            mySwipeRefreshLayout.setRefreshing(true);
        }
        getFirstPagerData();
    }

    @SuppressWarnings("unchecked")
    public void getData() {
            Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getRedPackgeTotalMomey("stealbonus/bonus_total", page
                    , Constants.PAGE_COUNT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SendRedTotals>() {
                        @Override
                        public void onCompleted() {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            mySwipeRefreshLayout.setRefreshing(false);
                            mAdapter.loadMoreFail();

                        }

                        @Override
                        public void onNext(SendRedTotals recorder) {
                            setData(recorder);
                        }
                    });
            addSubscription(subscription);
    }

    private void setData(SendRedTotals recorder) {
        List<SendRedTotals.Data> list = recorder.data;
        showToast(recorder.msg);
        if (page <= 0) {
            if (null == list || list.size() <= 0) {
                mAdapter.loadMoreEnd();
                //无数据 TODO 放置无数据页面
                mAdapter.setEmptyView(View.inflate(mContext, R.layout.library_module_no_data, null));
            } else {
                mAdapter.setNewData(list);//添加新的数据
                mAdapter.loadMoreComplete();//加载完成，上拉可以加更多
            }
        } else {
            if (list.size() >= Constants.PAGE_COUNT) {//每页30条数据
                mAdapter.loadMoreComplete();//加载完成，上拉可以加更多
            } else {
                mAdapter.loadMoreEnd();//没有更多的数据
            }
            mAdapter.addData(list);
        }
    }

    private void getFirstPagerData() {
        page = 0;
        getData();
    }

    private void getNextPagerData() {
        page++;
        getData();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.v3Back == id) {
            finish();
        }

    }
}

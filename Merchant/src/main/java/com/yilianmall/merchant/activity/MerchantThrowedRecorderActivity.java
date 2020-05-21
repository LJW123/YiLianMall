package com.yilianmall.merchant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.widget.MySwipeRefreshLayout;
import com.yilian.networkingmodule.entity.SendPackgeRecorder;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.ThrowedRedRecorderAdapter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * create  ZYH  2017/12/6
 */
public class MerchantThrowedRecorderActivity extends BaseSimpleActivity implements View.OnClickListener {
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ThrowedRedRecorderAdapter mAdapter;
    private int page = 0;
    private String id;
    private TextView v3Titile;
    private ImageView v3Back;
    private TextView tvContent;
    private TextView tvJiesou;
    private TextView tvAmount;
    private ImageView ivPhoto;
    private TextView overRed;
    private View headView;


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
                Intent intent = new Intent(mContext, MerchantStealedPackgeDetialsActivity.class);
                intent.putExtra("data", (SendPackgeRecorder.Data.RecordDetails) adapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    private void initView() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.merchant_color_status_redpackget));
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        v3Titile = (TextView) findViewById(R.id.v3Title);
        v3Titile.setText("商业奖励记录");
        v3Titile.setTextColor(getResources().getColor(R.color.merchant_white));
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setVisibility(View.VISIBLE);
        v3Back.setImageResource(R.mipmap.merchant_white_arrow);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.v3Layout);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.merchant_color_status_redpackget));
        mySwipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.merchant_swipe_refresh);
        mySwipeRefreshLayout.setMode(MySwipeRefreshLayout.Mode.PULL_FROM_START);
        mySwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.merchant_color_red));


        mRecyclerView = (RecyclerView) findViewById(R.id.merchant_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ThrowedRedRecorderAdapter(R.layout.merchant_item_packge_recorder, true);
        headView = LayoutInflater.from(mContext).inflate(R.layout.merchant_head_packge_recorder, null);
        ivPhoto = (ImageView) headView.findViewById(R.id.merchant_iv_image);

        tvContent = (TextView) headView.findViewById(R.id.merchant_tv_content);
        tvAmount = (TextView) headView.findViewById(R.id.merchant_tv_amount);
        tvJiesou = (TextView) headView.findViewById(R.id.merchant_tv_jiesou);
        overRed = (TextView) headView.findViewById(R.id.merchant_over_red);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setHeadData(SendPackgeRecorder recorder) {
        GlideUtil.showImageRadius(mContext, recorder.data.merchantImage, ivPhoto,6);
        tvContent.setText(recorder.data.merchantName);
        tvAmount.setText(recorder.data.amount);
        tvJiesou.setText(recorder.data.unlockAmount);
        overRed.setText(recorder.data.overAmount);

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
           Subscription subscription= RetrofitUtils3.getRetrofitService(mContext).getRedPackgeRecorder("stealbonus/bonus_info", page
                    , id, Constants.PAGE_COUNT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SendPackgeRecorder>() {
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
                        public void onNext(SendPackgeRecorder recorder) {
                            setData(recorder);
                        }
                    });
           addSubscription(subscription);
    }

    private void setData(SendPackgeRecorder recorder) {
        if (mAdapter.getHeaderLayoutCount()==0){
            setHeadData(recorder);
            mAdapter.setHeaderView(headView);
        }
        List<SendPackgeRecorder.Data.RecordDetails> list = recorder.data.list;
        showToast(recorder.msg);
        if (page <= 0) {
            if (null == list || list.size() <= 0) {
                mAdapter.loadMoreEnd();
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

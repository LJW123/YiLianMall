package com.yilianmall.merchant.activity;


import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;


import com.jaeger.library.StatusBarUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.widget.MySwipeRefreshLayout;
import com.yilian.networkingmodule.entity.SendPackgeRecorder;

import com.yilian.networkingmodule.entity.SendRedStealedDetails;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.adapter.StealedPackedDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * create  ZYH  2017/12/6
 */
public class MerchantStealedPackgeDetialsActivity extends BaseSimpleActivity implements View.OnClickListener {
    private MySwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private StealedPackedDetailsAdapter mAdapter;
    private TextView v3Titile;
    private ImageView v3Back;
    private ImageView headImage;
    private TextView headName;
    private TextView headCount;
    private TextView headJifen;
    private SendPackgeRecorder.Data.RecordDetails data;


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
                getData();
            }
        });
    }

    private void initView() {
        StatusBarUtil.setColor(this,getResources().getColor(R.color.merchant_color_status_redpackget));
        data = getIntent().getParcelableExtra("data");
        v3Titile = (TextView) findViewById(R.id.v3Title);
        v3Titile.setText("奖励被偷详情");
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
        mAdapter = new StealedPackedDetailsAdapter(R.layout.merchant_item_stealed_details, true);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.merchant_head_stealed_details, null);
        headImage = (ImageView) headView.findViewById(R.id.merchant_head_image);
        headName = (TextView) headView.findViewById(R.id.merchant_head_name);
        headCount = (TextView) headView.findViewById(R.id.merchant_head_count);
        headJifen = (TextView) headView.findViewById(R.id.merchant_head_jifen);
        setHeadData();

        mAdapter.setHeaderView(headView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setHeadData() {
        GlideUtil.showImageRadius(mContext, data.photo, headImage, 6);
        headName.setText(data.name);
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
        getData();
    }

    @SuppressWarnings("unchecked")
    public void getData() {
            //获取奖励
        Subscription subscription=RetrofitUtils3.getRetrofitService(mContext).getStealedRedDetails("stealbonus/user_steal_info",
                    data.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<SendRedStealedDetails>() {
                        @Override
                        public void onCompleted() {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(e.getMessage());
                            mySwipeRefreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onNext(SendRedStealedDetails recorder) {
                            mySwipeRefreshLayout.setRefreshing(false);
                            mAdapter.setNewData(recorder.data.list);//添加新的数据
                            headCount.setText("共领" + recorder.data.count + "个奖励");
                            headJifen.setText(recorder.data.integral + "奖券");
                        }
                    });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.v3Back == id) {
            finish();
        }

    }
}

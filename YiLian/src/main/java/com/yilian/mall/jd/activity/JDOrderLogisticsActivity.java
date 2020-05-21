package com.yilian.mall.jd.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.jd.adapter.JDOrderLogisticsAdapter;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDOrderLogisticsEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单物流信息
 *
 * @author Zg on 2017/5/27.
 */
public class JDOrderLogisticsActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;
    //订单编号
    private TextView tvOrderNumber;
    //内容列表
    private RecyclerView mRecyclerView;
    private JDOrderLogisticsAdapter mAdapter;
    /**
     * 京东订单号
     */
    private String jdOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_order_logistics);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new RefreshClickListener());
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        //订单编号
        tvOrderNumber = findViewById(R.id.tv_order_number);
        //内容列表
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void initData() {
        tvTitle.setText("订单跟踪");
        jdOrderId = getIntent().getStringExtra("jdOrderId");
        tvOrderNumber.setText("订单编号：" + jdOrderId);
        getJDOrderLogistics();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
    }


    /**
     * 查询物流信息
     */
    private void getJDOrderLogistics() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJDOrderLogistics("jd_orders/jd_orderTrack_info", jdOrderId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDOrderLogisticsEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDOrderLogisticsEntity entity) {
                        List<JDOrderLogisticsEntity.DataBean> mData = entity.data;
                        if (mData != null && mData.size() > 0) {
                            mAdapter = new JDOrderLogisticsAdapter(mData.size());
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.setNewData(mData);
                            varyViewUtils.showDataView();
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            default:
                break;
        }
    }


    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getJDOrderLogistics();
                }
            }, 1000);
        }
    }
}

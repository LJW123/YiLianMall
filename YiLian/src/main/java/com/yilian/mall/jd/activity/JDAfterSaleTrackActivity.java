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
import com.yilian.mall.jd.adapter.JDAfterSaleTrackAdapter;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleDetialEntity;

import java.util.Collections;
import java.util.List;


/**
 * 京东售后追踪
 *
 * @author Zg on 2017/5/28.
 */
public class JDAfterSaleTrackActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;

    private RecyclerView mRecyclerView;
    private JDAfterSaleTrackAdapter mAdapter;

    //京东售后详情 信息
    private List<JDAfterSaleDetialEntity.serviceTrackInfoDTOs> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_after_sale_track);
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

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    public void initData() {
        tvTitle.setText("服务单详情");
        mDatas = (List<JDAfterSaleDetialEntity.serviceTrackInfoDTOs>) getIntent().getSerializableExtra("serviceTrackInfoDTOsList");
        if (mDatas != null && mDatas.size() > 0) {
            //倒序排列
            Collections.reverse(mDatas);
            mAdapter = new JDAfterSaleTrackAdapter(mDatas.size());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setNewData(mDatas);
            varyViewUtils.showDataView();
        } else {
            varyViewUtils.showEmptyView();
        }
    }

    private void initListener() {
        ivBack.setOnClickListener(this);

    }


    /**
     * 按钮的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
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

                }
            }, 1000);
        }
    }
}

package com.yilian.mall.ctrip.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ctrip.adapter.CtripOrderProgressAdapter;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderDetailEntity;

import java.util.List;

/**
 * 携程 订单进度
 *
 * @author Created by Zg on 2018/9/20.
 */

public class CtripOrderProgressActivity extends BaseFragmentActivity implements View.OnClickListener {

    private VaryViewUtils varyViewUtils;
    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView mRecyclerView;
    private CtripOrderProgressAdapter mAdapter;

    private List<CtripOrderDetailEntity.Process> processList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ctrip_activity_order_progress);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varyViewUtils.showLoadingView();
            }
        });

        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    public void initData() {
        tvTitle.setText("订单进度");

        processList = (List<CtripOrderDetailEntity.Process>) getIntent().getSerializableExtra("processList");

        if (processList != null && processList.size() > 0) {
            mAdapter=new CtripOrderProgressAdapter(processList.size());
            mAdapter.bindToRecyclerView(mRecyclerView);
            mAdapter.setNewData(processList);
            varyViewUtils.showDataView();
        } else {
            varyViewUtils.showEmptyView();
        }
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
    }






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
}

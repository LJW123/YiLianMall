package com.leshan.ylyj.view.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.adapter.TreasureAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.bean.TreasureBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 百宝箱
 */
public class TreasureActivity extends BaseActivity {

    private RecyclerView treasure_rv;
    private TreasureAdapter adapter;
    private List<TreasureBean> treasureBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);


        initToolbar();
        setToolbarTitle("百宝箱");
        hasBack(true);

        initView();
        initListener();
        initData();

//        presenter = new TreasurePresenter(this);
//        presenter.deliverList(this, "");
    }

    @Override
    protected void initView() {
        treasure_rv = (RecyclerView) findViewById(R.id.treasure_rv);
        treasure_rv.setLayoutManager(new GridLayoutManager(this, 5, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * 6);
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        treasureBeans = new ArrayList<>();

        List<TreasureBean.Treasure> data = new ArrayList<>();
        data.add(new TreasureBean.Treasure(R.mipmap.all, ""));
        data.add(new TreasureBean.Treasure(R.mipmap.all, ""));
        data.add(new TreasureBean.Treasure(R.mipmap.all, ""));
        data.add(new TreasureBean.Treasure(R.mipmap.all, ""));
        treasureBeans.add(new TreasureBean(0, data));
        treasureBeans.add(new TreasureBean(0, data));
        treasureBeans.add(new TreasureBean(0, data));
        treasureBeans.add(new TreasureBean(0, data));
        treasureBeans.add(new TreasureBean(0, data));
        adapter = new TreasureAdapter(treasureBeans, this);
        treasure_rv.setAdapter(adapter);

    }


}

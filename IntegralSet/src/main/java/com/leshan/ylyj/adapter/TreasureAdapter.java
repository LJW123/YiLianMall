package com.leshan.ylyj.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.bean.TreasureBean;
import com.leshan.ylyj.bean.TwoPageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class TreasureAdapter extends BaseQuickAdapter<TreasureBean, BaseViewHolder> {

    private Context mContenxt;
    private TwoPageAdapter adapter;
    private List<TwoPageBean> data;

    public TreasureAdapter(List data, Context context) {
        super(R.layout.item_treasure_layout, data);
        this.mContenxt = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TreasureBean item) {

        helper.setImageResource(R.id.icon_iv, R.mipmap.myyijia);
        RecyclerView treasure_item_rv = helper.getView(R.id.treasure_item_rv);
        treasure_item_rv.setLayoutManager(new GridLayoutManager(mContext, 5, LinearLayoutManager.VERTICAL, false) {

            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
                if (getChildCount() > 0) {
                    View firstChildView = recycler.getViewForPosition(0);
                    measureChild(firstChildView, widthSpec, heightSpec);
                    setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), 200);
                } else {
                    super.onMeasure(recycler, state, widthSpec, heightSpec);

                }
            }
        });

        treasure_item_rv.setNestedScrollingEnabled(false);

        data = new ArrayList<>();
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        data.add(new TwoPageBean(R.mipmap.all, "全部"));
        adapter = new TwoPageAdapter(data);
        treasure_item_rv.setAdapter(adapter);

    }
}

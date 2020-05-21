package com.yilian.mall.ctrip.adapter;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ctrip.CtripOrderDetailEntity;

import java.text.SimpleDateFormat;

/**
 * 携程 订单进度
 *
 * @author Created by Zg on 2018/9/20.
 */

public class CtripOrderProgressAdapter extends BaseQuickAdapter<CtripOrderDetailEntity.Process, BaseViewHolder> {
    private int listSize;

    public CtripOrderProgressAdapter(int listSize) {
        super(R.layout.ctrip_item_order_progress);
        this.listSize = listSize;
    }

    @Override
    protected void convert(BaseViewHolder helper, CtripOrderDetailEntity.Process item) {
        //处理不同条目展示不同效果
        helper.getView(R.id.rl_end).setVisibility(View.GONE);
        helper.getView(R.id.rl_middle).setVisibility(View.GONE);
        helper.getView(R.id.rl_head).setVisibility(View.GONE);

        TextView tv_date = helper.getView(R.id.tv_date);
        tv_date.setTextColor(Color.parseColor("#999999"));
        TextView tv_time = helper.getView(R.id.tv_time);
        tv_time.setTextColor(Color.parseColor("#999999"));
        TextView tv_content = helper.getView(R.id.tv_content);
        tv_content.setTextColor(Color.parseColor("#999999"));

        if (listSize > 1 && helper.getLayoutPosition() == 0) {
            helper.getView(R.id.rl_end).setVisibility(View.VISIBLE);
            tv_date.setTextColor(Color.parseColor("#4289FF"));
            tv_time.setTextColor(Color.parseColor("#4289FF"));
            tv_content.setTextColor(Color.parseColor("#4289FF"));
        } else if (helper.getLayoutPosition() == listSize - 1) {
            helper.getView(R.id.rl_head).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.rl_middle).setVisibility(View.VISIBLE);
        }
        tv_date.setText(new SimpleDateFormat("MM-dd").format(item.time * 1000));
        tv_time.setText(new SimpleDateFormat("HH:mm").format(item.time * 1000));
        tv_content.setText(item.msg);
    }
}

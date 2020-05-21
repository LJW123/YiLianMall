package com.yilian.mall.adapter;

import android.content.Context;

import com.yilian.mall.R;
import com.yilian.mall.entity.OrderRouting;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class OrderRoutingAdapter extends SimpleAdapter<OrderRouting> {
    int listSize;

    public OrderRoutingAdapter(Context context,ArrayList<OrderRouting> list) {
        super(context, R.layout.item_order_routing,list);
        listSize = list.size();
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, OrderRouting item,int position) {
        if (position==listSize-1)
            viewHolder.getImageView(R.id.iv).setImageResource(R.mipmap.ic_order_routing_start);

        viewHolder.getTextView(R.id.tv_min).setText(item.min);
        viewHolder.getTextView(R.id.tv_day).setText(item.describe+"\n"+item.day);

    }

}

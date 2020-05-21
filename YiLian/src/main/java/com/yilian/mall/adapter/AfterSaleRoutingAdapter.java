package com.yilian.mall.adapter;

import android.content.Context;
import android.view.View;

import com.yilian.mall.R;
import com.yilian.mall.utils.StringFormat;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/20.
 */
public class AfterSaleRoutingAdapter extends SimpleAdapter<String> {
    int listSize;

    public AfterSaleRoutingAdapter(Context context, ArrayList<String> list) {
        super(context, R.layout.item_after_sale_routing,list);
        listSize = list.size();
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item,int position) {
        if (listSize-2 == position){
            viewHolder.getImageView(R.id.iv).setImageResource(R.mipmap.ic_after_sale_route_choose);
            viewHolder.getTextView(R.id.tv).setTextColor(context.getResources().getColor(R.color.color_red));
        }

        if (listSize-1 == position){
            viewHolder.getImageView(R.id.iv).setImageResource(R.mipmap.ic_after_sale_route_end);
            viewHolder.getTextView(R.id.tv).setVisibility(View.GONE);
        }

        if (item.split(",")[0].equals("00")){
            viewHolder.getTextView(R.id.tv).setText(item.split(",")[1]+
                    "\n  " );
        }else{
            viewHolder.getTextView(R.id.tv).setText(item.split(",")[1]+
                    "\n"+ StringFormat.formatDate(item.split(",")[0]));
        }


    }

}

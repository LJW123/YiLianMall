package com.yilian.mall.adapter;/**
 * Created by  on 2017/1/10 0010.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.MTCodesEntity;

import java.util.ArrayList;

/**
 * Created by  on 2017/1/10 0010.
 */
public class MTPackageTicketsAdapter extends android.widget.BaseAdapter {
    private final String isDelivery;
    private Context context;
    private ArrayList<MTCodesEntity> list;

    public MTPackageTicketsAdapter(Context context, ArrayList<MTCodesEntity> list, String isDelivery) {
        this.context = context;
        this.list = list;
        this.isDelivery =isDelivery;
    }

    @Override
    public int getCount() {
        if (list.size() == 0 || list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ticket_mt, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int num = position + 1;
        holder.tv.setText("券码" + num +"：");

        holder.tvNum.setText(list.get(position).code);
        if ("0".equals(isDelivery)){
            holder.iv.setVisibility(View.VISIBLE);
        } else {
            holder.iv.setVisibility(View.INVISIBLE);
        }

        String stateStr = null;
        switch (list.get(position).status) {
            case "1":
                stateStr = "未消费";
                holder.tvState.setTextColor(context.getResources().getColor(R.color.color_red));
                break;
            case "2":
                stateStr = "已消费";
                holder.tvState.setTextColor(context.getResources().getColor(R.color.color_999));
                holder.iv.setClickable(false);
                break;
            case "3":
                stateStr = "待退款";
                holder.tvState.setTextColor(context.getResources().getColor(R.color.regist_code));
                holder.iv.setClickable(false);
                break;
            case "4":
                stateStr = "已退款";
                holder.tvState.setTextColor(context.getResources().getColor(R.color.color_register));
                holder.iv.setClickable(false);
                break;
            case "":
                stateStr = "";
                holder.iv.setClickable(false);
                break;
        }
        holder.tvState.setText(stateStr);

        return convertView;
    }

    class ViewHolder {
        TextView tv;
        TextView tvNum;
        ImageView iv;
        TextView tvState;
    }
}

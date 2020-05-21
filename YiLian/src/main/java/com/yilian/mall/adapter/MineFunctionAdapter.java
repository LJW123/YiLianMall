package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MineIconsEntity;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/12/4 0004.
 */

public class MineFunctionAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private ArrayList<MineIconsEntity.IconsBean> list;

    public MineFunctionAdapter(Context mContext, ArrayList<MineIconsEntity.IconsBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (null == list || list.size() == 0) {
            return 0;
        } else {
            return list.size();
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mine_mall, null);

            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GlideUtil.showImage(mContext, list.get(position).img, holder.iv);

        holder.tv.setText(list.get(position).title);

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}

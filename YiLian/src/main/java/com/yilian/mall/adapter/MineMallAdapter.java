package com.yilian.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MallBeanEntity;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/12/4 0004.
 */

public class MineMallAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private ArrayList<MallBeanEntity> list;

    public MineMallAdapter(Context mContext, ArrayList<MallBeanEntity> list) {
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
            holder.ivLine = (ImageView) convertView.findViewById(R.id.iv_line);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvTop = convertView.findViewById(R.id.tv_top);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if ((position + 1) == list.size()) {
            holder.ivLine.setVisibility(View.VISIBLE);
        } else {
            holder.ivLine.setVisibility(View.GONE);
        }

        if ("1".equals(list.get(position).contentType)) {
            holder.iv.setVisibility(View.GONE);
            holder.tvTop.setVisibility(View.VISIBLE);
            holder.tvTop.setText(list.get(position).num);
        } else {
            holder.iv.setVisibility(View.VISIBLE);
            holder.tvTop.setVisibility(View.GONE);
            GlideUtil.showImage(mContext, list.get(position).img, holder.iv);
        }

        holder.tv.setText(list.get(position).title);

        if (!TextUtils.isEmpty(list.get(position).count)) {
            if ("0".equals(list.get(position).count)) {
                holder.tvCount.setVisibility(View.GONE);
            } else {
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.tvCount.setText(list.get(position).count);
            }
        } else {
            holder.tvCount.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivLine;
        ImageView iv;
        TextView tvTop;
        TextView tv;
        TextView tvCount;
    }
}

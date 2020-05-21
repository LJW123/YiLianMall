package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.MTOrderEntity;
import com.yilian.mall.widgets.NoScrollListView;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/12/6 0006.
 * 套餐的adapter
 */

public class MTComboAdapter extends android.widget.BaseAdapter{
    private Context context;
    private ArrayList<MTOrderEntity.PackageInfo> list;
    private ArrayList<MTOrderEntity.PackageInfo.Content> innerList;

    public MTComboAdapter(Context context, ArrayList<MTOrderEntity.PackageInfo> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_combo_mt, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.listView = (NoScrollListView) convertView.findViewById(R.id.listView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(list.get(position).packageName);
        innerList = list.get(position).content;
        MTComboListAdapter adapter = new MTComboListAdapter(context, innerList);
        holder.listView.setAdapter(adapter);

        return convertView;
    }

    class ViewHolder {
        TextView tv;
        NoScrollListView listView;
    }

    /**
     * 嵌套的listview的adapter
     */
    class MTComboListAdapter extends android.widget.BaseAdapter {
        private Context context;
        private ArrayList<MTOrderEntity.PackageInfo.Content> innerList;

        public MTComboListAdapter(Context context, ArrayList<MTOrderEntity.PackageInfo.Content> innerList) {
            this.context = context;
            this.innerList = innerList;
        }

        @Override
        public int getCount() {
            if (innerList.size() == 0 || innerList == null) {
                return 0;
            }
            return innerList.size();
        }

        @Override
        public Object getItem(int position) {
            return innerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InnerViewHolder innerHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_combo_list_mt, null);
                innerHolder = new InnerViewHolder();
                innerHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                innerHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
                innerHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                convertView.setTag(innerHolder);
            } else {
                innerHolder = (InnerViewHolder) convertView.getTag();
            }

            innerHolder.tvName.setText(innerList.get(position).name);
            innerHolder.tvCount.setText(innerList.get(position).number + "份");
            innerHolder.tvPrice.setText(innerList.get(position).cost + "元");

            return convertView;
        }
    }

    class InnerViewHolder {
        TextView tvName;
        TextView tvCount;
        TextView tvPrice;
    }

}

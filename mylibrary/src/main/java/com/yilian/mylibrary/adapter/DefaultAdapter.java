package com.yilian.mylibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class DefaultAdapter<T> extends BaseAdapter {
    private int itemCounts;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected ArrayList<T> mDatas;
    protected final int mItemLayoutId;

    public DefaultAdapter(Context context, ArrayList<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public DefaultAdapter(Context context, ArrayList<T> mDatas, int itemLayoutId, int itemCounts) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.itemCounts = itemCounts;
    }


    @Override
    public int getCount() {
        if (itemCounts == 0) {
            return mDatas.size();
        } else {
            if (itemCounts > mDatas.size()) {
                return mDatas.size();
            } else {
                return itemCounts;
            }
        }
    }

    public boolean isLastItem(int position) {
        position = position + 1;
        if (itemCounts > mDatas.size()) {
            boolean b = mDatas.size() == position;
            return b;
        } else {
            boolean b = itemCounts == position;
            return b;
        }
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, T item, int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

}

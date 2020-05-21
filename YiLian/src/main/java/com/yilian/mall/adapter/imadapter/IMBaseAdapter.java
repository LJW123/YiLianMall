package com.yilian.mall.adapter.imadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * author XiuRenLi on 2016/8/18  PRAY NO BUG
 */

public  class IMBaseAdapter<T>  extends BaseAdapter{

    protected ArrayList<T> list;

    @Override
    public int getCount() {
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
        return convertView;
    }
}

package com.yilianmall.merchant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


/**
 * Created by liuyuqi on 2017/6/13 0013.
 */

public abstract class BaseListAdapter<T> extends BaseAdapter {
    public final Context mContext;
    public ArrayList<T> datas;

    public BaseListAdapter(Context mContext, ArrayList datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas.size() < 0) {
            return 0;
        } else {
            return datas.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}

package com.leshan.ylyj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import rxfamily.entity.BranchBankEntity;


/**
 * Created by Ray_L_Pain on 2017/10/20 0020.
 */

public class BranchBankListAdapter extends android.widget.BaseAdapter {
    private Context context;
    private ArrayList<BranchBankEntity.ListBean> branchList;


    public BranchBankListAdapter(Context context, ArrayList<BranchBankEntity.ListBean> branchList) {
        this.context = context;
        this.branchList = branchList;
    }

    @Override
    public int getCount() {
        if (branchList.size() != 0) {
            return branchList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return branchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TvViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_tv, null);
            holder = new TvViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (TvViewHolder) convertView.getTag();
        }

        BranchBankEntity.ListBean bean = branchList.get(position);
        holder.tv.setText(bean.branchName);
        Logger.i("ray-" + bean.branchName);
        return convertView;
    }

    class TvViewHolder {
        public TextView tv;
    }
}

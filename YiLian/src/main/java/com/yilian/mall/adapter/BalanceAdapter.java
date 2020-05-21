package com.yilian.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.AssetsRecordList;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;

import java.util.ArrayList;

/**
 * Created by yukun on 2016/3/30.
 */
public class BalanceAdapter extends android.widget.BaseAdapter {
    private String c;
    private String type;

    private TextView tvNumber;
    private TextView tvStatus;
    private LinearLayout ll;

    private Context context;

    private ArrayList<AssetsRecordList.AssetsRecord> datas;

    public BalanceAdapter(Context context, String c, String type, ArrayList<AssetsRecordList.AssetsRecord> datas) {
        this.c = c;
        this.type = type;
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        AssetsRecordList.AssetsRecord data = datas.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_balance, null);

            viewHolder.tvDescribe = (TextView) convertView.findViewById(R.id.tv_describe);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTime.setText(StringFormat.formatDate(data.payTime));
        viewHolder.tvDescribe.setText(data.typeMsg);
        tvNumber = viewHolder.tvNumber;
        if (this.type.equals("2")) {//领奖励记录时显示领奖励状态布局
            tvStatus = viewHolder.tvStatus;
            viewHolder.ll.setVisibility(View.VISIBLE);
        }

        switch (type) {
            case "0":
                viewHolder.tv.setText("获得时间：");
                break;
            case "1":
                viewHolder.tv.setText("使用时间：");
                break;
            case "2":
                viewHolder.tv.setText("领奖励时间：");
                break;
        }

        switch (c) {
            case "lefen":
                lefen(data);
                break;

            case "lebi":
                lebi(data);

                break;

            case "coupon":
                coupon(data);
                break;
            case "voucher":
                voucher(data);
                break;

        }

        return convertView;
    }



    private final class ViewHolder {
        TextView tvDescribe;
        TextView tvNumber;
        TextView tvStatus;
        TextView tv;
        TextView tvTime;
        LinearLayout ll;
    }
    private void voucher(AssetsRecordList.AssetsRecord data) {
        switch (this.type) {
            case "0": //获得
                tvNumber.setText("+ " + MoneyUtil.getLeXiangBi(data.payCount));
                break;
            case "1"://消费
                tvNumber.setText("- " +MoneyUtil.getLeXiangBi(data.payCount));
                break;

        }
    }
    void lefen(AssetsRecordList.AssetsRecord item) {
        switch (this.type) {
            case "0": //获得
                tvNumber.setText("+ " + item.payCount);
                break;
            case "1"://消费
                tvNumber.setText("- " + item.payCount);
                break;

        }

    }

    void lebi(AssetsRecordList.AssetsRecord item) {
        switch (this.type) {
            case "0":
                tvNumber.setText("+ " + String.format("%.2f", Float.parseFloat(item.payCount) / 100));
                break;

            case "1":
                tvNumber.setText("- " + String.format("%.2f", Float.parseFloat(item.payCount) / 100));
                break;

            case "2":
                tvNumber.setText("¥ " + String.format("%.2f", Float.parseFloat(item.payCount) / 100));
                if (item.type == 0)
                    tvStatus.setText("待审核");
                else
                    tvStatus.setText("领奖励成功");
                break;
        }

    }

    void coupon(AssetsRecordList.AssetsRecord item) {
        switch (this.type) {
            case "0":
                tvNumber.setText("+ " + String.format("%.2f", Float.parseFloat(item.payCount) / 100));
                break;

            case "1":
                tvNumber.setText("- " + String.format("%.2f", Float.parseFloat(item.payCount) / 100));
                break;

        }
    }
}

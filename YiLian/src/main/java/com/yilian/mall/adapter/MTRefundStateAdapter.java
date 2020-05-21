package com.yilian.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.entity.MTRefundStatusEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yilian.mall.R.id.top_line;

/**
 * Created by Ray_L_Pain on 2016/12/6 0006.
 * 美团-退款详情-退款流程adapter
 */

public class MTRefundStateAdapter extends android.widget.BaseAdapter{
    private Context context;
    private MTRefundStatusEntity info;
    private String status;

    public MTRefundStateAdapter(Context context, MTRefundStatusEntity info) {
        this.context = context;
        this.info = info;
        status = info.status;//1待处理 2退款中 3退款失败
    }

    @Override
    public int getCount() {
//        if ("1".equals(status)){
//            return 1;
//        } else {
//            return 2;
//        }
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return info;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_refund_detail_mt, null);
            holder = new ViewHolder();
            holder.topLine = convertView.findViewById(top_line);
            holder.bottomLine = convertView.findViewById(R.id.bottom_line);
            holder.circle = (TextView) convertView.findViewById(R.id.circle);
            holder.tvPoint = (TextView) convertView.findViewById(R.id.tv_point);
            holder.tvDetail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.first = (LinearLayout) convertView.findViewById(R.id.first);
            holder.second = (LinearLayout) convertView.findViewById(R.id.second);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_nothing);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        switch (status) {
            case "1":
                if (position == 0) {
                    holder.topLine.setVisibility(View.INVISIBLE);
                    holder.bottomLine.setVisibility(View.VISIBLE);
                    holder.circle.setBackgroundResource(R.drawable.bg_solid_red_90);
                    holder.tvPoint.setText("申请退款");
                    holder.tvDetail.setVisibility(View.INVISIBLE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                    String time = sdf.format(new Date(Long.valueOf(info.createTime + "000")));
                    holder.tvTime.setText(time);
                } else {
                    holder.topLine.setVisibility(View.VISIBLE);
                    holder.bottomLine.setVisibility(View.INVISIBLE);
                    holder.circle.setBackgroundResource(R.drawable.bg_solid_red_90);
                    holder.tvPoint.setText("系统处理中");
                    holder.tvDetail.setVisibility(View.INVISIBLE);
                    holder.tvTime.setVisibility(View.INVISIBLE);
                }
                break;
            case "2":
                if (position == 0) {
                    holder.topLine.setVisibility(View.INVISIBLE);
                    holder.bottomLine.setVisibility(View.VISIBLE);
                    holder.circle.setBackgroundResource(R.drawable.bg_solid_red_90);
                    holder.tvPoint.setText("申请退款");
                    holder.tvDetail.setVisibility(View.INVISIBLE);
                    String time = sdf.format(new Date(Long.valueOf(info.createTime + "000")));
                    holder.tvTime.setText(time);
                } else {
                    holder.topLine.setVisibility(View.VISIBLE);
                    holder.bottomLine.setVisibility(View.INVISIBLE);
                    holder.tvPoint.setText("退款成功");
                    holder.tvDetail.setVisibility(View.INVISIBLE);
                    if (TextUtils.isEmpty(info.datetime)) {
                        String time = sdf.format(new Date(Long.valueOf(info.planTime + "000")));
                        holder.tvTime.setText(time);
                    } else {
                        String time = sdf.format(new Date(Long.valueOf(info.datetime + "000")));
                        holder.tvTime.setText(time);
                    }
                }
                break;
            case "3":
                if (position == 0) {
                    holder.topLine.setVisibility(View.INVISIBLE);
                    holder.bottomLine.setVisibility(View.VISIBLE);
                    holder.circle.setBackgroundResource(R.drawable.bg_solid_red_90);
                    holder.tvPoint.setText("申请退款");
                    holder.tvDetail.setVisibility(View.INVISIBLE);
                    String time = sdf.format(new Date(Long.valueOf(info.createTime + "000")));
                    holder.tvTime.setText(time);
                } else {
                    holder.topLine.setVisibility(View.VISIBLE);
                    holder.bottomLine.setVisibility(View.INVISIBLE);
                    holder.tvPoint.setText("退款失败");
                    holder.tvDetail.setVisibility(View.VISIBLE);
                    holder.tvDetail.setText("请联系益联益家：0371-55090326");
                    if (TextUtils.isEmpty(info.datetime)) {
                        String time = sdf.format(new Date(Long.valueOf(info.planTime + "000")));
                        holder.tvTime.setText(time);
                    } else {
                        String time = sdf.format(new Date(Long.valueOf(info.datetime + "000")));
                        holder.tvTime.setText(time);
                    }
                }
                break;
            default:
                holder.first.setVisibility(View.GONE);
                holder.second.setVisibility(View.GONE);
                holder.iv.setVisibility(View.VISIBLE);
                break;
        }

        return convertView;
    }

    class ViewHolder {
        View topLine;
        View bottomLine;
        TextView circle;
        TextView tvPoint;
        TextView tvDetail;
        TextView tvTime;
        LinearLayout first;
        LinearLayout second;
        ImageView iv;
    }
}

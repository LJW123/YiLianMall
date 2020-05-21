package com.leshan.ylyj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leshan.ylyj.testfor.R;

import java.util.List;

import rxfamily.entity.ShortcutDonateEntity;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class DonationIntegralAdapter extends RecyclerView.Adapter<DonationIntegralAdapter.MyViewHolder> {
    private Context context;
    private List<ShortcutDonateEntity.LintegralListBean> data;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private MyViewHolder holder;//
    private int layoutPosition;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data, int position);
    }

    public DonationIntegralAdapter(Context context, List<ShortcutDonateEntity.LintegralListBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//
        View itemView = View.inflate(context, R.layout.item_donation_integral, null);
        holder = new MyViewHolder(itemView);//

        return holder;
    }

    @Override

    public void onBindViewHolder(final MyViewHolder holder, final int position) {//
        holder.donation_integral_integral_tv.setText(data.get(position).getIntegral() + "奖券");//
        holder.donation_integral_credit_tv.setText(String.valueOf(data.get(position).getIntegralCredit()));
        holder.itemView.setTag(String.valueOf(data.get(position).getIntegral()));//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的位置
                layoutPosition = holder.getLayoutPosition();
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(holder.itemView, String.valueOf(holder.itemView.getTag()), layoutPosition);
            }
        });

        //更改状态
        if (position == layoutPosition) {
            holder.donation_integral_ll.setBackgroundResource(R.drawable.red_ellipse_side_line);//
            holder.donation_integral_integral_tv.setTextColor(Color.WHITE);
            holder.donation_integral_credit_tv.setTextColor(Color.WHITE);
        } else {
            holder.donation_integral_ll.setBackgroundResource(R.drawable.gray_ellipse_side_line);//
            holder.donation_integral_integral_tv.setTextColor(Color.BLACK);
            holder.donation_integral_credit_tv.setTextColor(Color.GRAY);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout donation_integral_ll;
        private final TextView donation_integral_integral_tv;
        private TextView donation_integral_credit_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            donation_integral_integral_tv = (TextView) itemView.findViewById(R.id.donation_integral_integral_tv);
            donation_integral_ll = (LinearLayout) itemView.findViewById(R.id.donation_integral_ll);
            donation_integral_credit_tv = (TextView) itemView.findViewById(R.id.donation_integral_credit_tv);
        }
    }
}

package com.leshan.ylyj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.bean.ShareCreditBean;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

import rxfamily.entity.ShareCreditEntity;

/**
 *  zhouhongyuan on 2017/12/12 0012.
 */

public class ShareCreditAdapter extends RecyclerView.Adapter<ShareCreditAdapter.MyViewHolder> {
    private Context context;
    private List<ShareCreditEntity.DataBean> data;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private MyViewHolder holder;
    private int layoutPosition;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data, int position);
    }

    public ShareCreditAdapter(Context context, List<ShareCreditEntity.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.share_credit_background_picture, null);
        holder = new MyViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.share_credit_style_tv.setText(data.get(position).getTitle());
        GlideUtil.showImageRadius(context, data.get(position).getMainPicture(), holder.share_credit_img,2);
        holder.itemView.setTag(data.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的位置
                layoutPosition = holder.getLayoutPosition();
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(holder.itemView, (String) holder.itemView.getTag(), layoutPosition);
            }
        });
        //更改状态
        if (position == layoutPosition) {
            holder.share_credit_selected_ll.setVisibility(View.VISIBLE);
            holder.share_credit_style_tv.setVisibility(View.GONE);
        } else {
            holder.share_credit_selected_ll.setVisibility(View.GONE);
            holder.share_credit_style_tv.setVisibility(View.VISIBLE);
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
        private LinearLayout share_credit_selected_ll;
        private TextView share_credit_style_tv;
        private ImageView share_credit_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            share_credit_style_tv = (TextView) itemView.findViewById(R.id.share_credit_style_tv);
            share_credit_selected_ll = (LinearLayout) itemView.findViewById(R.id.share_credit_selected_ll);
            share_credit_img=itemView.findViewById(R.id.share_credit_img);
        }
    }

    public ShareCreditEntity.DataBean getSelectedData(){
        if (data.size()<=0){
            return null;
        }else {
            return data.get(layoutPosition);
        }
    }
}

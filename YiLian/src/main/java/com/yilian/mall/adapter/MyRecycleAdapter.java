package com.yilian.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;

import java.util.ArrayList;



/**
 * Created by liuyuqi on 2017/4/27 0027.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter{
    private final ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> prizeList;
    private Context mContext;

    public MyRecycleAdapter(Context mContext, ArrayList<WheelOfFortuneAwardListEntity.DataBean.ListBean> prizeList) {
        this.mContext = mContext;
        this.prizeList =prizeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder=new MyViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.dailprize_list_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return prizeList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvPrizeNumber;
        TextView tvPrizeGoods;
        TextView tvPrizeTime;
        TextView tvStateTag;
        ImageView ivState;

        public MyViewHolder(View itemView) {
            super(itemView);


        }
    }
}

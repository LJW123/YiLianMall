package com.yilian.mall.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.networkingmodule.entity.WheelOfFortuneAwardListEntity;

import java.util.List;

/**
 * Created by liuyuqi on 2017/4/28 0028.
 */
public class MyPrizeAdapter extends BaseListAdapter<WheelOfFortuneAwardListEntity.DataBean.ListBean> {

    public MyPrizeAdapter(List<WheelOfFortuneAwardListEntity.DataBean.ListBean> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        MyHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.dailprize_list_item, null);
            holder = new MyHolder(view);
            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }

        WheelOfFortuneAwardListEntity.DataBean.ListBean listBean = datas.get(position);
        holder.tvPrizeNumber.setText(listBean.orderId);
        holder.tvPrizeDownTime.setText("中奖时间" + DateUtils.timeStampToStr2(Long.parseLong(listBean.winningTime)));
        holder.tvPrizeUnuseTime.setText("兑换时间" + DateUtils.timeStampToStr2(Long.parseLong(listBean.unuseTime)));

        String skuStr = "";
        for (int i = 0; i < listBean.normsList.size(); i++) {
            if (i == listBean.normsList.size() - 1) {
                skuStr += listBean.normsList.get(i);
            } else {
                skuStr += listBean.normsList.get(i) + ",";
            }
        }
        listBean.skuStr = skuStr;
        switch (listBean.round) {
            case "0"://未领取
                holder.llltemContent.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.conversion_bg_white));
                holder.tvStateTag.setVisibility(View.VISIBLE);
                holder.tvStateTag.setText("立\n即\n领\n取");
                holder.tvStateTag.setTextColor(context.getResources().getColor(R.color.color_now_get));
                break;
            case "1"://已兑换
                holder.llltemContent.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.conversion_bg_gray));
                holder.ivState.setVisibility(View.VISIBLE);
                break;
            case "2"://已失效
                holder.llltemContent.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.conversion_bg_gray));
                holder.tvStateTag.setVisibility(View.VISIBLE);
                holder.tvStateTag.setText("已\n失\n效");
                holder.tvStateTag.setTextColor(context.getResources().getColor(R.color.color_lose_efficacy));
                break;
        }

        if (listBean.type == 0) {
            holder.tvPrizeName.setText(listBean.goodName);
            holder.tvPrizeName.setTextColor(context.getResources().getColor(R.color.color_red));
            holder.tvPrizeName.setBackgroundColor(Color.TRANSPARENT);
        } else {
            holder.tvPrizeName.setText(listBean.goodName);
            holder.tvPrizeName.setBackgroundColor(context.getResources().getColor(R.color.color_red));
            holder.tvPrizeName.setTextColor(Color.WHITE);
        }

        holder.llltemContent.setVisibility(View.VISIBLE);
        return view;
    }

    class MyHolder {
        LinearLayout llltemContent;
        TextView tvPrizeNumber;
        TextView tvPrizeName;
        TextView tvPrizeDownTime;
        TextView tvPrizeUnuseTime;
        TextView tvStateTag;
        ImageView ivState;


        public MyHolder(View itemView) {
            this.llltemContent = (LinearLayout) itemView.findViewById(R.id.ll_ltem_content);
            this.tvPrizeNumber = (TextView) itemView.findViewById(R.id.tv_prize_number);
            this.tvPrizeName = (TextView) itemView.findViewById(R.id.tv_prize_name);
            this.tvPrizeDownTime = (TextView) itemView.findViewById(R.id.tv_done_time);
            this.tvPrizeUnuseTime = (TextView) itemView.findViewById(R.id.tv_unuse_time);
            this.tvStateTag = (TextView) itemView.findViewById(R.id.tv_state_tag);
            this.ivState = (ImageView) itemView.findViewById(R.id.iv_as_converion);
        }
    }


}

package com.yilian.mall.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.networkingmodule.entity.AssetsRecordList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuqi on 2017/6/8 0008.
 */
public class MyBalanceAdapter extends RecyclerView.Adapter<MyBalanceAdapter.BalanceHolder> {
    private final Context mContext;
    private final ArrayList<AssetsRecordList.AssetsList> list;
    private String lastMonth;

    public MyBalanceAdapter(Context mContext, ArrayList<AssetsRecordList.AssetsList> lists, List<Object>... value) {
        this.mContext = mContext;
        this.list = lists;

    }


    @Override
    public BalanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BalanceHolder(View.inflate(mContext, R.layout.item_my_balance, null));
    }

    @Override
    public void onBindViewHolder(BalanceHolder holder, int position) {
        AssetsRecordList.AssetsList assetsList = list.get(position);
        holder.tvMsg.setText(assetsList.typeMsg);
        holder.tvTime.setText(DateUtils.timeStampToStr(Long.parseLong(assetsList.payTime)));
        if (Integer.parseInt(assetsList.type) > 100) {
            //减少
            holder.tvMoney.setText("-" + MoneyUtil.getLeXiangBi(assetsList.payCount));
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.color_my_bule));
            holder.ivIcon.setImageResource(R.mipmap.ic_balance_item_bule);
        } else {
            //增加
            holder.tvMoney.setText("+" + MoneyUtil.getLeXiangBi(assetsList.payCount));
            holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.myinfo_red_bg));
            holder.ivIcon.setImageResource(R.mipmap.ic_balance_item_red);
        }
        //领奖励状态判断
        if ("108".equals(assetsList.type)) {//已驳回
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(assetsList.balanceText);
            switch (assetsList.balanceStatus) {
                case "2"://已驳回
                    holder.tvStatus.setTextColor(Color.WHITE);
                    holder.tvStatus.setBackgroundResource(R.drawable.balance_screen_item_bg);
                    holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VersionDialog.Builder builder = new VersionDialog.Builder(mContext);
                            builder.setTitle("温馨提示");
                            builder.setMessage(assetsList.refuseText);
                            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setCancelable(true);
                            builder.create().show();
                        }
                    });

                    break;
                default:
                    holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_detail_status));
                    holder.tvStatus.setBackgroundResource(R.drawable.balance_screen_item_bg_white);
                    holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    break;
            }

        } else {
            holder.tvStatus.setVisibility(View.GONE);
        }
        holder.tvMonth.setText(assetsList.month + "月");

        //第一条默认显示
        if (position == 0) {
            holder.tvMonth.setVisibility(View.GONE);//外层列表隐藏月份
//            holder.tvMonth.setVisibility(View.VISIBLE);
        } else {
            //那当前的数据跟上一条的数据 position--  ;做对比如果不同显示月份
            if (position >= 1) {
                position--;
                lastMonth = list.get(position).month;
                if (!lastMonth.equals(assetsList.month)) {
                    holder.tvMonth.setVisibility(View.GONE);//外层列表隐藏月份
//                    holder.tvMonth.setVisibility(View.VISIBLE);
                } else {
                    holder.tvMonth.setVisibility(View.GONE);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BalanceHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvMsg;
        TextView tvMoney;
        TextView tvTime;
        TextView tvStatus;
        TextView tvMonth;

        public BalanceHolder(View itemView) {
            super(itemView);
            this.ivIcon = (ImageView) itemView.findViewById(R.id.iv);
            this.tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            this.tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            this.tvMsg = (TextView) itemView.findViewById(R.id.tv_describe);
            this.tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            this.tvMonth = (TextView) itemView.findViewById(R.id.tv_item_mouth);
        }
    }
}

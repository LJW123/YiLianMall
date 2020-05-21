package com.yilian.mall.adapter;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.SpellGroupOrderActivity;
import com.yilian.mall.ui.SpellGroupResultStatusActivity;
import com.yilian.mall.ui.V3MoneyActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.CountdownView.CountdownView;
import com.yilian.mall.widgets.CountdownView.DynamicConfig;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MyGroupsListEntity;

import java.util.List;

public class MyGroupsListAdapter extends BaseListAdapter<MyGroupsListEntity.DataBean> {
    private MyGroupListHolder holder;

    public MyGroupsListAdapter(List<MyGroupsListEntity.DataBean> datas) {
        super(datas);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item_my_spell_groups_list, null);
            holder = new MyGroupListHolder(view);
            view.setTag(holder);
        } else {
            holder = (MyGroupListHolder) view.getTag();
        }
        MyGroupsListEntity.DataBean listBean = datas.get(position);

        if (TextUtils.isEmpty(listBean.groupCondition) || TextUtils.isEmpty(listBean.joinAmount)) {
            holder.tvGroupCondition.setVisibility(View.VISIBLE);
            holder.tvGroupCondition.setText(listBean.groupCondition + "人团");
        } else {
            holder.tvGroupCondition.setVisibility(View.GONE);
        }
        switch (listBean.status) {
            case "0":
                holder.tvSpellGroupStatus.setText("活动结束");
                holder.tvGroupCondition.setVisibility(View.INVISIBLE);
                holder.tvSepllGroupDeatils.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setText("在开一团");
                holder.tvLookOrder.setBackgroundResource(R.drawable.bg_empty_red_circle);
                holder.tvLookOrder.setTextColor(context.getResources().getColor(R.color.color_red));
                holder.tvDownTime.setVisibility(View.GONE);
                holder.countDownTimer.setVisibility(View.GONE);
                holder.tvNowInvite.setVisibility(View.GONE);
                holder.ivWining.setVisibility(View.GONE);
                holder.tvHasNumber.setVisibility(View.GONE);
                break;
            case "1":
                holder.tvSpellGroupStatus.setText("拼团中");
                holder.tvDownTime.setVisibility(View.VISIBLE);
                holder.countDownTimer.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(listBean.time) && !TextUtils.isEmpty(listBean.endTime)) {
                    long differenceTime = Long.parseLong(listBean.endTime) - Long.parseLong(listBean.time);
                    Logger.i("difference Time  " + differenceTime);
                    if (differenceTime > 0) {
                        holder.countDownTimer.setVisibility(View.VISIBLE);
                        holder.tvDownTime.setVisibility(View.VISIBLE);
                        holder.countDownTimer.start(differenceTime * 1000);
                        holder.countDownTimer.dynamicShow(new DynamicConfig.Builder().setConvertDaysToHours(true).build());
                    } else {
                        holder.countDownTimer.setVisibility(View.GONE);
                        holder.tvDownTime.setVisibility(View.GONE);
                    }
                } else {
                    holder.tvDownTime.setVisibility(View.GONE);
                    holder.countDownTimer.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(listBean.groupCondition) && !TextUtils.isEmpty(listBean.joinAmount)) {
                    int hasNumber = Integer.parseInt(listBean.groupCondition) - Integer.parseInt(listBean.joinAmount);
                    if (hasNumber > 0) {
                        holder.tvHasNumber.setVisibility(View.VISIBLE);
                        holder.tvHasNumber.setText(Html.fromHtml("<font color=\"#666666\">还差</font>" + hasNumber + "<font color=\"#666666\">人</font>"));
                        holder.tvNowInvite.setVisibility(View.VISIBLE);
                    }
                }
                holder.ivWining.setVisibility(View.GONE);
                holder.tvLookOrder.setVisibility(View.GONE);
                holder.tvSepllGroupDeatils.setVisibility(View.GONE);
                break;
            case "2"://显示共几人拼团
                holder.tvSpellGroupStatus.setText("拼团失败");
                holder.tvSepllGroupDeatils.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(listBean.groupCondition)&&!TextUtils.isEmpty(listBean.joinAmount)){
                    holder.tvGroupCondition.setVisibility(View.VISIBLE);
                    holder.tvGroupCondition.setText(listBean.groupCondition+"人团");
                }
                holder.tvLookOrder.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setText("查看退款");
                holder.tvHasNumber.setVisibility(View.GONE);
                holder.tvLookOrder.setBackgroundResource(R.drawable.bg_corners_white);
                holder.tvLookOrder.setTextColor(context.getResources().getColor(R.color.color_666));
                holder.tvDownTime.setVisibility(View.GONE);
                holder.countDownTimer.setVisibility(View.GONE);
                holder.tvNowInvite.setVisibility(View.GONE);
                holder.ivWining.setVisibility(View.GONE);
                break;
            case "3"://显示共几人拼团
                holder.tvSpellGroupStatus.setText("拼团成功");
                if (!TextUtils.isEmpty(listBean.groupCondition)&&!TextUtils.isEmpty(listBean.joinAmount)){
                    holder.tvGroupCondition.setVisibility(View.VISIBLE);
                    holder.tvGroupCondition.setText(listBean.groupCondition+"人团");
                }
                holder.tvSepllGroupDeatils.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setText("在开一团");
                holder.tvLookOrder.setBackgroundResource(R.drawable.bg_corners_white);
                holder.tvLookOrder.setTextColor(context.getResources().getColor(R.color.color_red));
                holder.ivWining.setVisibility(View.GONE);
                holder.tvDownTime.setVisibility(View.GONE);
                holder.countDownTimer.setVisibility(View.GONE);
                holder.tvNowInvite.setVisibility(View.GONE);
                break;
            case "4":
                holder.tvSpellGroupStatus.setText("未中奖");
                holder.tvGroupCondition.setVisibility(View.INVISIBLE);
                holder.tvSepllGroupDeatils.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setText("查看退款");
                holder.tvLookOrder.setBackgroundResource(R.drawable.bg_corners_white);
                holder.tvLookOrder.setTextColor(context.getResources().getColor(R.color.color_666));
                holder.tvDownTime.setVisibility(View.GONE);
                holder.countDownTimer.setVisibility(View.GONE);
                holder.tvHasNumber.setVisibility(View.GONE);
                holder.tvNowInvite.setVisibility(View.GONE);
                holder.ivWining.setVisibility(View.GONE);
                break;
            case "5":
                holder.ivWining.setVisibility(View.GONE);
                holder.tvSpellGroupStatus.setText("已中奖");
                holder.tvGroupCondition.setVisibility(View.INVISIBLE);
                holder.tvSepllGroupDeatils.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setText("查看订单");
                holder.tvLookOrder.setBackgroundResource(R.drawable.bg_corners_white);
                holder.tvLookOrder.setTextColor(context.getResources().getColor(R.color.color_666));
                holder.ivWining.setVisibility(View.VISIBLE);
                holder.tvLookOrder.setVisibility(View.VISIBLE);
                holder.tvDownTime.setVisibility(View.GONE);
                holder.countDownTimer.setVisibility(View.GONE);
                holder.tvNowInvite.setVisibility(View.GONE);
                holder.ivWining.setVisibility(View.VISIBLE);
                break;
        }
        holder.tvTag.setVisibility(View.VISIBLE);
        holder.tvTag.setText(listBean.label);
        String imageUrl = listBean.goodsIcon;
        if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
            imageUrl = imageUrl + Constants.ImageSuffix;
        } else {
            imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
        }
        GlideUtil.showImage(context, imageUrl, holder.ivIcon);
        holder.tvGoodsName.setText(listBean.goodsName);
        holder.tvSku.setText(listBean.goodsSkuName);
        holder.tvGoodsCount.setText("X1");
        holder.tvPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(listBean.groupPrice)));

        holder.rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SpellGroupOrderActivity.class);
                intent.putExtra("orderId", listBean.orderId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.tvNowInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转分享结果界面
                Intent intent = new Intent(context, SpellGroupResultStatusActivity.class);
                intent.putExtra("groupId", listBean.groupId);
                intent.putExtra("activityId",listBean.activityId);
                intent.putExtra("orderId",listBean.orderId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.tvSepllGroupDeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SpellGroupResultStatusActivity.class);
                intent.putExtra("groupId", listBean.groupId);
                intent.putExtra("activityId",listBean.activityId);
                intent.putExtra("orderId",listBean.orderId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.tvLookOrder.setOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {
                switch (holder.tvLookOrder.getText().toString().trim()) {
                    case "在开一团"://TODO传递的值要改
                        intent = new Intent(context, SpellGroupResultStatusActivity.class);
                        intent.putExtra("groupId", listBean. groupId);
                        intent.putExtra("activityId",listBean.activityId);
                        intent.putExtra("orderId",listBean.orderId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case "查看订单":
                        intent = new Intent(context, SpellGroupOrderActivity.class);
                        intent.putExtra("orderId", listBean.orderId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case "查看退款":
                        intent = new Intent(context, V3MoneyActivity.class);
                        intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }

    class MyGroupListHolder {
        RelativeLayout rlContent;
        TextView tvSpellGroupStatus;
        TextView tvTag;
        ImageView ivIcon;
        TextView tvGoodsName;
        TextView tvSku;
        TextView tvGoodsCount;
        TextView tvPrice;
        CountdownView countDownTimer;
        TextView tvSepllGroupDeatils;
        TextView tvLookOrder;
        ImageView ivWining;
        TextView tvGroupCondition;
        TextView tvDownTime;
        TextView tvHasNumber;
        TextView tvNowInvite;

        public MyGroupListHolder(View itemView) {
            this.rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            this.tvSpellGroupStatus = (TextView) itemView.findViewById(R.id.tv_spell_group_status);
            this.tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            this.ivIcon = (ImageView) itemView.findViewById(R.id.imageView4);
            this.tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            this.tvSku = (TextView) itemView.findViewById(R.id.tv_sku);
            this.tvGoodsCount = (TextView) itemView.findViewById(R.id.tv_goods_count);
            this.tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            this.countDownTimer = (CountdownView) itemView.findViewById(R.id.countdownview);
            this.tvDownTime = (TextView) itemView.findViewById(R.id.downtime);
            this.tvGroupCondition = (TextView) itemView.findViewById(R.id.tv_group_condition);
            this.tvSepllGroupDeatils = (TextView) itemView.findViewById(R.id.tv_spell_group_details);
            this.tvLookOrder = (TextView) itemView.findViewById(R.id.tv_look_order);
            this.ivWining = (ImageView) itemView.findViewById(R.id.iv_wining);
            this.tvHasNumber = (TextView) itemView.findViewById(R.id.tv_has_number);
            this.tvNowInvite = (TextView) itemView.findViewById(R.id.tv_now_invite);
        }
    }

}
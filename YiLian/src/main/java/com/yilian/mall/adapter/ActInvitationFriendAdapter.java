package com.yilian.mall.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ActInventIntegralChangeListEntity;

/**
 *  @author by LYQ on 2017/11/6.
 * 邀请好友赚领奖励
 */

public class ActInvitationFriendAdapter extends BaseQuickAdapter<ActInventIntegralChangeListEntity.DataBean, BaseViewHolder> {

    public ActInvitationFriendAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActInventIntegralChangeListEntity.DataBean item) {

        TextView tvRank = helper.getView(R.id.tv_rank);
        ImageView ivRank = helper.getView(R.id.iv_rank);

        int position1 = helper.getLayoutPosition();
        switch (position1) {
            case 1:
                tvRank.setVisibility(View.GONE);
                ivRank.setVisibility(View.VISIBLE);
                ivRank.setImageResource(R.mipmap.act_invent_one);
                break;
            case 2:
                tvRank.setVisibility(View.GONE);
                ivRank.setVisibility(View.VISIBLE);
                ivRank.setImageResource(R.mipmap.act_invent_two);
                break;
            case 3:
                tvRank.setVisibility(View.GONE);
                ivRank.setVisibility(View.VISIBLE);
                ivRank.setImageResource(R.mipmap.act_invent_three);
                break;
            default:
                tvRank.setVisibility(View.VISIBLE);
                tvRank.setText(String.valueOf(position1));
                ivRank.setVisibility(View.GONE);
                break;
        }

        ImageView ivPhoto = helper.getView(R.id.iv_user);
        GlideUtil.showCirImage(mContext, item.photo, ivPhoto);

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.name)) {
            tvName.setText("暂无昵称");
        } else {
            tvName.setText(item.name);
        }

        TextView tvMoney = helper.getView(R.id.tv_money);
        tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(item.get_integral));

    }

}

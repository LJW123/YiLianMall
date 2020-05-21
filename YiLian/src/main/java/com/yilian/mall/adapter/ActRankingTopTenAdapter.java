package com.yilian.mall.adapter;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.networkingmodule.entity.ActClockInTopTenEntity;

import java.text.SimpleDateFormat;

/**
 * @author LYQ  2017/11/10.
 */

public class ActRankingTopTenAdapter extends BaseQuickAdapter<ActClockInTopTenEntity.ListBean, BaseViewHolder> {
    public ActRankingTopTenAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActClockInTopTenEntity.ListBean item) {
        helper.addOnClickListener(R.id.tv_like_count)
                .addOnClickListener(R.id.tv_reply)
                .addOnClickListener(R.id.tv_give_reward)
                .addOnClickListener(R.id.iv_icon);
        if (item.isPraise == 0) {
            ((TextView) helper.getView(R.id.tv_like_count)).setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.icon_unlike), null, null, null);
        } else {
            ((TextView) helper.getView(R.id.tv_like_count)).setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.icon_like), null, null, null);
        }
        helper.setText(R.id.tv_name, item.name);
        GlideUtil.showCirImage(mContext, WebImageUtil.getInstance().getWebImageUrlNOSuffix(item.photo), helper.getView(R.id.iv_icon));
        helper.setText(R.id.tv_date, TimeUtils.millis2String(item.signinAt*1000L,new SimpleDateFormat("HH:mm:ss")));
        String integral="起早打卡瓜分"+MoneyUtil.getLeXiangBiNoZero(item.givenIntegral)+"奖券";
        SpannableString stringSpanned=new SpannableString(integral);
        stringSpanned.setSpan(new ForegroundColorSpan(Color.parseColor("#00BCA4")),6,integral.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_carve_up, stringSpanned);
        helper.setText(R.id.tv_like_count, String.valueOf(item.praise));
        helper.setText(R.id.tv_reply, String.valueOf(item.comment));
        helper.setText(R.id.iv_level, String.valueOf(helper.getLayoutPosition()));
        switch (helper.getLayoutPosition()) {
            case 1:
                helper.setBackgroundRes(R.id.iv_level, R.mipmap.icon_atc_level_01);
                break;
            case 2:
                helper.setBackgroundRes(R.id.iv_level, R.mipmap.icon_atc_level_02);
                break;
            case 3:
                helper.setBackgroundRes(R.id.iv_level, R.mipmap.icon_atc_level_03);
                break;
            default:
                helper.setBackgroundRes(R.id.iv_level, R.mipmap.icon_atc_level_04);
                break;
        }
    }
}

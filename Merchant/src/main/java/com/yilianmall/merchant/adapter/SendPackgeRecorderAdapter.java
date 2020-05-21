package com.yilianmall.merchant.adapter;

import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.SendRedTotals;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zyh
 */

public class SendPackgeRecorderAdapter extends BaseQuickAdapter<SendRedTotals.Data, BaseViewHolder> {
    public boolean hasHead;

    public SendPackgeRecorderAdapter(@LayoutRes int layoutResId, boolean hasHead) {
        super(layoutResId);
        this.hasHead = hasHead;
    }

    @Override
    protected void convert(BaseViewHolder helper, SendRedTotals.Data item) {
        //头像
        GlideUtil.showImageRadius(mContext, item.merchantImage, (ImageView) helper.getView(R.id.merchant_photo),6);
        if (!TextUtils.isEmpty(item.createdAt)){
            String time=TimeUtils.millis2String(Long.parseLong(item.createdAt)*1000L,new SimpleDateFormat("MM-dd HH:mm:ss"));
            helper.setText(R.id.merchant_apply_time,time);
        }else {
            helper.setText(R.id.merchant_apply_time,"");
        }

        helper.setText(R.id.merchant_name, item.merchantName);
        helper.setText(R.id.merchant_red_amounts, "奖励数量：" + item.quantity);
        helper.setText(R.id.merchant_red_over, "奖励金额：" + item.amount);
        TextView jieSouRed = helper.getView(R.id.merchant_jiesou_counts);
        String str = "奖励解锁：<font color='#FF0000'>" + item.unlockAmount + "</font>";
        jieSouRed.setText(Html.fromHtml(str));
        if (item.isOverdue) {
            helper.getView(R.id.merchant_iv_pass_time).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.merchant_iv_pass_time).setVisibility(View.GONE);
        }
    }
}

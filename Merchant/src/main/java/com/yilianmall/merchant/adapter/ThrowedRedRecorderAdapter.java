package com.yilianmall.merchant.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.SendPackgeRecorder;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zyh
 */

public class ThrowedRedRecorderAdapter extends BaseQuickAdapter<SendPackgeRecorder.Data.RecordDetails, BaseViewHolder> {
    public boolean hasHead;

    public ThrowedRedRecorderAdapter(@LayoutRes int layoutResId, boolean hasHead) {
        super(layoutResId);
        this.hasHead = hasHead;
    }

    @Override
    protected void convert(BaseViewHolder helper, SendPackgeRecorder.Data.RecordDetails item) {
        GlideUtil.showImageRadius(mContext, item.photo, (ImageView) helper.getView(R.id.merchant_iv_photo),6);
        helper.setText(R.id.merchant_name, item.name);
        helper.setText(R.id.merchant_countss, item.applyAt);
        setItemAmount((TextView) helper.getView(R.id.merchant_countss), item.applyAt);
        if (item.applyAt.contains("-")){
            ((TextView) helper.getView(R.id.merchant_countss)).setTextColor(Color.parseColor("#DE554D"));
        }else {
            ((TextView) helper.getView(R.id.merchant_countss)).setTextColor(Color.parseColor("#999999"));
        }
        helper.setText(R.id.merchant_time,  DateUtils.timeStampToStr5(Long.parseLong(item.applyAt)));
    }


    public void setItemAmount(TextView counts, String amount) {
            amount=formatIntegrals(amount);
            int start = amount.indexOf(".");
            Spannable sp = new SpannableString(amount);
            sp.setSpan(new AbsoluteSizeSpan(25, true), 0, start+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            counts.setText(sp);
    }
    private String formatIntegrals(String integral) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        if (TextUtils.isEmpty(integral)) {
            return "0.00";
        } else {
            if (integral.contains(".")) {
                int index = integral.indexOf(".");
                if (index == 0) {
                    integral = 0 + integral + 0;
                } else {
                    if (index == integral.length() - 1) {
                        integral = integral + 0;
                    }
                }
            }
            BigDecimal bd = new BigDecimal(integral);
            integral = df.format(bd.setScale(2, BigDecimal.ROUND_DOWN));//截取末尾两位小数而不四舍5入
            return integral;
        }
    }

}

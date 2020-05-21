package com.yilian.mall.adapter;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.StealMyRedpackgetsDetails;

/**
 * Created by ZYH on 2017/12/2 0002.
 */

public class RedPackgeDetailsAdapter extends BaseQuickAdapter<StealMyRedpackgetsDetails.Data, BaseViewHolder> {
    private boolean isHasHeader = false;

    public RedPackgeDetailsAdapter(int layoutResId, boolean isHasHeader) {
        super(layoutResId);
        this.isHasHeader = isHasHeader;
    }

    @Override
    protected void convert(BaseViewHolder helper, StealMyRedpackgetsDetails.Data item) {
        if (null != item) {
            helper.setText(R.id.name, item.name);
            helper.setText(R.id.time, item.applyAt);
            setItemAmount(helper.getView(R.id.redpackgers_counts), item.amount);
            GlideUtil.showCirHeadNoSuffix(mContext, item.photo, helper.getView(R.id.iv_stealman_pohto));
        }
    }

    //设置textView不同字体
    public void setItemAmount(TextView counts, String amount) {
        if (TextUtils.isEmpty(amount)) {
            Spannable sp = new SpannableString("0.00");
            sp.setSpan(new AbsoluteSizeSpan(17, true), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            counts.setText(sp);
        } else {
            if (amount.contains(".")) {
                int start = amount.indexOf(".");
                Spannable sp = new SpannableString(amount);
                sp.setSpan(new AbsoluteSizeSpan(17, true), 0, start, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                counts.setText(sp);
            } else {
                amount = amount + ".00";
                int start = amount.indexOf(".");
                Spannable sp = new SpannableString(amount);
                sp.setSpan(new AbsoluteSizeSpan(17, true), 0, start, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                counts.setText(sp);
            }

        }


    }


}

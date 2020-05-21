package com.yilian.mall.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.networkingmodule.entity.ActSignInData;

import java.util.List;

/**
 * @author Created by  on 2017/11/21 0021.
 */

public class SignInDaysAdapter extends BaseQuickAdapter<ActSignInData.DatelistBean, com.chad.library.adapter.base.BaseViewHolder> {
    private final String todayMonth;
    private final int isTodaySign;

    public SignInDaysAdapter(@LayoutRes int layoutResId, @Nullable List<ActSignInData.DatelistBean> data, String todayMonth, int isTodaySign) {
        super(layoutResId, data);
        this.todayMonth = todayMonth;
        this.isTodaySign = isTodaySign;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActSignInData.DatelistBean item) {
        ((TextView) helper.getView(R.id.tv_day)).setText(item.days);
        if (item.isday == 1) {
            ((TextView) helper.getView(R.id.tv_month)).setText(todayMonth);
            if (isTodaySign == 1) {
                ((TextView) helper.getView(R.id.tv_day)).setTextColor(Color.parseColor("#FFC257"));
                ((TextView) helper.getView(R.id.tv_month)).setTextColor(Color.parseColor("#FFC257"));
            } else {
                ((TextView) helper.getView(R.id.tv_day)).setTextColor(Color.WHITE);
                ((TextView) helper.getView(R.id.tv_month)).setTextColor(Color.WHITE);

            }
        } else {
            ((TextView) helper.getView(R.id.tv_day)).setTextColor(ContextCompat.getColor(mContext, R.color.color_999));
            ((TextView) helper.getView(R.id.tv_month)).setText("");
        }
    }
}

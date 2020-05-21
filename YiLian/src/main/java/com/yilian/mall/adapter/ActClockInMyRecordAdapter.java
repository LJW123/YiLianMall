package com.yilian.mall.adapter;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.networkingmodule.entity.ActClockInMyRecordEntity;

/**
 * @author Created by  on 2017/11/23 0023.
 */

public class ActClockInMyRecordAdapter extends BaseQuickAdapter<ActClockInMyRecordEntity.RecordListBean, com.chad.library.adapter.base.BaseViewHolder> {

    public ActClockInMyRecordAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, ActClockInMyRecordEntity.RecordListBean item) {
        helper.setText(R.id.tv_date, item.punchStr);
        if (item.status == 0) {
            ((TextView) helper.getView(R.id.tv_clock_in_status)).setTextColor(Color.parseColor("#FF7B11"));
            helper.setText(R.id.tv_clock_in_status, "打卡失败");
            ((TextView) helper.getView(R.id.tv_get_integral)).setTextColor(Color.parseColor("#FF7B11"));
            helper.setText(R.id.tv_get_integral, "-" + MoneyUtil.getLeXiangBiNoZero(item.applyIntegral));
        } else {
            ((TextView) helper.getView(R.id.tv_clock_in_status)).setTextColor(Color.parseColor("#00BCA4"));
            helper.setText(R.id.tv_clock_in_status, "打卡成功");
            ((TextView) helper.getView(R.id.tv_get_integral)).setTextColor(Color.parseColor("#00BCA4"));
            helper.setText(R.id.tv_get_integral, "+" + MoneyUtil.getLeXiangBiNoZero(item.givenIntegral));
        }
    }
}
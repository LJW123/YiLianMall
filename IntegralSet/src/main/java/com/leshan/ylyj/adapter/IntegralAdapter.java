package com.leshan.ylyj.adapter;


import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;

import java.util.List;

/**
 * 奖券 下部 列表
 */

public class IntegralAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Activity mActivity;

    public IntegralAdapter(Activity mActivity, List data) {
        super(R.layout.item_integral, data);
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        if (Double.valueOf(item) >= 0) {
            helper.setText(R.id.money_tv, item);
            helper.setTextColor(R.id.money_tv, mActivity.getResources().getColor(R.color.price));
        } else {
            helper.setText(R.id.money_tv, item);
            helper.setTextColor(R.id.money_tv, mActivity.getResources().getColor(R.color.gray));
        }
    }
}

package com.leshan.ylyj.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import rxfamily.entity.ExchangeIndexRecordEntity;

/**
 * 消费指数
 */

public class ExchangeIndexRecordAdapter extends BaseQuickAdapter<ExchangeIndexRecordEntity.LastSevenBean, BaseViewHolder> {
    private Activity mActivity;

    public ExchangeIndexRecordAdapter(Activity mActivity) {
        super(R.layout.item_exchange_index_record);
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ExchangeIndexRecordEntity.LastSevenBean item) {
        helper.setText(R.id.date_tv, item.getTime());
        helper.setText(R.id.earnings_tv, item.getIndex());
        helper.setText(R.id.range_tv, item.getRiseFall());
        //1涨 2跌  0 平
        if (Integer.parseInt(item.getPlusorsub()) == 0) {
            helper.setTextColor(R.id.range_tv, mActivity.getResources().getColor(R.color.price));
            helper.getView(R.id.index_iv).setVisibility(View.INVISIBLE);
        } else if (Integer.parseInt(item.getPlusorsub()) == 1) {
            helper.setTextColor(R.id.range_tv, mActivity.getResources().getColor(R.color.price));
            helper.getView(R.id.index_iv).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.index_iv, R.mipmap.index_up);
        } else {
            helper.setTextColor(R.id.range_tv, Color.parseColor("#2A9D2D"));
            helper.getView(R.id.index_iv).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.index_iv, R.mipmap.index_des);
        }
    }

}

package com.yilian.mall.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.V3MoneyDetailWithDrawEntity;

import java.util.List;

/**
 * @author Created by  on 2017/12/14.
 *         提取营收或奖励详情中的领奖励详情里面的领取状态列表
 */

public class V3MoneyWithDrawDetailProgressAdapter extends BaseQuickAdapter<V3MoneyDetailWithDrawEntity.ExtractStatusBean, com.chad.library.adapter.base.BaseViewHolder> {
    /**
     * 提取营收 108
     * 奖励   默认非108
     */
    public int dataType = 0;

    public V3MoneyWithDrawDetailProgressAdapter(int layoutResId, @Nullable List<V3MoneyDetailWithDrawEntity.ExtractStatusBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, V3MoneyDetailWithDrawEntity.ExtractStatusBean item) {
//        GlideUtil.showCirIconNoSuffix(mContext, item.image, helper.getView(R.id.iv_icon));
        helper.setText(R.id.tv_status, item.status);
        helper.setText(R.id.tv_time, item.time);
        if (helper.getLayoutPosition() == mData.size() - 1) {
//            最后一条
            helper.getView(R.id.view_line).setVisibility(View.GONE);
            if (dataType == Constants.TYPE_EXTRACT_REVENUE_108 && item.resultStatus == 1) {
                helper.setTextColor(R.id.tv_status, Color.parseColor("#43B946"));
                ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.icon_v3_money_withdraw_status_suc);
            } else {
                helper.setTextColor(R.id.tv_status, Color.parseColor("#F25024"));
                ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.icon_v3_money_withdraw_status_1);
            }

        } else {
            helper.getView(R.id.view_line).setVisibility(View.VISIBLE);
            helper.setTextColor(R.id.tv_status, ContextCompat.getColor(mContext, R.color.color_999));
            ((ImageView) helper.getView(R.id.iv_icon)).setImageResource(R.mipmap.icon_v3_money_withdraw_status_0);
        }
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
}

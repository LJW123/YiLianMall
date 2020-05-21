package com.yilian.mall.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mall.R;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.RemoveRedRecordEntity;

import java.util.Calendar;

/**
 * @author Created by Ray_L_Pain on 2017/11/22 0022.
 */

public class RedPacketFragmentAdapter extends BaseQuickAdapter<RemoveRedRecordEntity.DataBean, com.chad.library.adapter.base.BaseViewHolder> {
    private Activity activity;

    public RedPacketFragmentAdapter(@LayoutRes int layoutResId, Activity activity) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoveRedRecordEntity.DataBean item) {

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirHeadNoSuffix(activity, item.photo, iv);

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.name);

        TextView tvMoney = helper.getView(R.id.tv_money);
        tvMoney.setText(MoneyUtil.getLeXiangBiNoZero(item.amount));

        TextView tvDay = helper.getView(R.id.tv_day);
        Calendar calendar = Calendar.getInstance();
        String millionSecond = item.created_at + "000";
        calendar.setTimeInMillis(NumberFormat.convertToLong(millionSecond, 0L));
        tvDay.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日奖励");
//        tvDay.setText("领取" + DateUtils.convertTimeToFormat(Long.parseLong(item.created_at)));

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(DateUtils.timeStampToStr5(Long.parseLong(item.apply_at)));
    }
}

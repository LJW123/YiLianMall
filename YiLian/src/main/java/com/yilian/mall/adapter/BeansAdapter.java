package com.yilian.mall.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.BeanEntity;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 益豆明细适配器
 *
 * @author zhaiyaohua on 2018/2/13 0013.
 */

public class BeansAdapter extends BaseQuickAdapter<BeanEntity.BeansData, com.chad.library.adapter.base.BaseViewHolder> {


    public BeansAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, BeanEntity.BeansData item) {
        View timeView = helper.getView(R.id.include_item_list_titile);
        View dataView = helper.getView(R.id.inlude_item_money_list);
        helper.getView(R.id.sticky_date_picker_ll).setVisibility(View.GONE);
        if (item instanceof BeanEntity.Statistics) {
            BeanEntity.Statistics statistics = (BeanEntity.Statistics) item;
            //月份
            timeView.setVisibility(View.VISIBLE);
            dataView.setVisibility(View.GONE);
            helper.setText(R.id.tv_month, ((BeanEntity.Statistics) item).date);
            helper.setText(R.id.tv_income_expend, ("收入"+Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(statistics.income) + " 支出"+Constants.APP_PLATFORM_DONATE_NAME + MoneyUtil.getLeXiangBiNoZero(statistics.expend)));
        } else if (item instanceof BeanEntity.DataList) {
            BeanEntity.DataList dataList = (BeanEntity.DataList) item;
            timeView.setVisibility(View.GONE);
            dataView.setVisibility(View.VISIBLE);
            GlideUtil.showImage(mContext, dataList.img, helper.getView(R.id.iv_icon));

            helper.setText(R.id.tv_detail, dataList.typeMsg);


            if (TimeUtils.isToday(dataList.time * 1000)) {
                helper.setText(R.id.tv_day, "今日 " + TimeUtils.millis2String(dataList.time * 1000, new SimpleDateFormat("HH:mm", Locale.getDefault())));
            } else if (TimeUtils.isToday(dataList.time * 1000 + Constants.ONE_DAY)) {
                helper.setText(R.id.tv_day, "昨日 " + TimeUtils.millis2String(dataList.time * 1000, new SimpleDateFormat("HH:mm", Locale.getDefault())));
            } else {
                helper.setText(R.id.tv_day, DateUtils.timeStampToMDHM(dataList.time));
            }

            if (dataList.type < 100) {
                helper.setText(R.id.tv_amount, "+" + com.yilian.mall.utils.MoneyUtil.getLeXiangBiNoZero(dataList.amount));
            } else {
                helper.setText(R.id.tv_amount, "-" + com.yilian.mall.utils.MoneyUtil.getLeXiangBiNoZero(dataList.amount));
            }

        }

    }
}

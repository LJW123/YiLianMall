package com.yilian.mall.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.V3MoneyDetailActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.TimeUtils;
import com.yilian.networkingmodule.entity.MoneyListEntity;
import com.yilian.networkingmodule.entity.V3MoneyListBaseData;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Created by  on 2017/12/13.
 */

public class V3MoneyListAdapter<T extends V3MoneyListBaseData> extends BaseMultiItemQuickAdapter<T, com.chad.library.adapter.base.BaseViewHolder> {

    /**
     * 0 奖励 1 奖券  108提取营收页面  3代购券
     */
    private int type;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public V3MoneyListAdapter(List<T> data, int type) {
        super(data);
        addItemType(V3MoneyListBaseData.LIST_CONTENT, R.layout.item_money_list);
        addItemType(V3MoneyListBaseData.LIST_TITLE, R.layout.item_money_list_title);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, T item) {
        switch (item.getItemType()) {
            case V3MoneyListBaseData.LIST_CONTENT:
                MoneyListEntity.ListBean itemContent = (MoneyListEntity.ListBean) item;
                GlideUtil.showCirIconNoSuffix(mContext, itemContent.img, helper.getView(R.id.iv_icon));
                helper.setText(R.id.tv_detail, itemContent.typeMsg);
                if (TimeUtils.isToday(itemContent.payTime * 1000)) {
                    helper.setText(R.id.tv_day, "今日 " + TimeUtils.millis2String(itemContent.payTime * 1000, new SimpleDateFormat("HH:mm", Locale.getDefault())));
                } else if (TimeUtils.isToday(itemContent.payTime * 1000 + Constants.ONE_DAY)) {
                    helper.setText(R.id.tv_day, "昨日 " + TimeUtils.millis2String(itemContent.payTime * 1000, new SimpleDateFormat("HH:mm", Locale.getDefault())));
                } else {
                    helper.setText(R.id.tv_day, DateUtils.timeStampToMDHM(itemContent.payTime));
                }
                String sign = "";
                if (itemContent.getIsMoney().equals("0"))//0 奖励 1 奖券
                    sign = "¥";
//                变化金额
                if (itemContent.type < 100) {
                    helper.setText(R.id.tv_amount, "+" + sign + MoneyUtil.getLeXiangBiNoZero(itemContent.payCount));
                } else {
                    helper.setText(R.id.tv_amount, "-" + sign + MoneyUtil.getLeXiangBiNoZero(itemContent.payCount));
                }
//                状态
                if (itemContent.type == 108) {
                    helper.setText(R.id.tv_status, itemContent.balanceText);
                    helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                } else {
                    if ("1".equals(itemContent.balanceStatus)) {
                        helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_status, "待结算");
                    } else {
                        helper.getView(R.id.tv_status).setVisibility(View.INVISIBLE);
                        helper.setText(R.id.tv_status, "");
                    }
                }


                break;
            case V3MoneyListBaseData.LIST_TITLE:
                MoneyListEntity.StatisticsBean itemHeader = (MoneyListEntity.StatisticsBean) item;
                String[] yearMonth = itemHeader.date.split("-");
                String year = yearMonth[0];
                String month = yearMonth[1];
//                String thisYear = String.valueOf(DateUtils.getYear());
//                if (year.equals(thisYear)) {
//                    helper.setText(R.id.tv_month, month + "月");
////                    测试跨年数据
//                    helper.setText(R.id.tv_month, year + "年" + month + "月");
//                } else {
                helper.setText(R.id.tv_month, year + "年" + month + "月");
//                }
                if (type == V3MoneyDetailActivity.TYPE_3) {
                    helper.setText(R.id.tv_income_expend, "获得代购券" + MoneyUtil.getLeXiangBiNoZero(itemHeader.income) + " 使用代购券" + MoneyUtil.getLeXiangBiNoZero(itemHeader.expend));
                } else {
                    if (itemHeader.getIsMoney().equals("0")) {
                        helper.setText(R.id.tv_income_expend, "收入奖励 ¥" + MoneyUtil.getLeXiangBiNoZero(itemHeader.income) + " 支出奖励 ¥" + MoneyUtil.getLeXiangBiNoZero(itemHeader.expend));
                    } else {
                        helper.setText(R.id.tv_income_expend, "收入奖券" + MoneyUtil.getLeXiangBiNoZero(itemHeader.income) + " 支出奖券" + MoneyUtil.getLeXiangBiNoZero(itemHeader.expend));
                    }
                }
                Logger.i("month:" + month + "  year:" + year);
                break;
            default:
                break;
        }
    }
}

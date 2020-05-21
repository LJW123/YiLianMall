package com.yilian.mall.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.networkingmodule.entity.V3MoneyDealingsEntity;
import com.yilian.networkingmodule.entity.V3MoneyListBaseData;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Created by  on 2017/12/14.
 */

public class V3MoneyDealingsListAdapter<T extends V3MoneyListBaseData> extends BaseMultiItemQuickAdapter<T, com.chad.library.adapter.base.BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    /**
     * 控制漂浮头部和item收支的显示位数
     */
    public static DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    /**
     * 转赠列表类型
     * 0  奖券转赠类型
     * 1  益豆转赠类型
     * 默认是0
     */
    public static final int DONATION_INTERGRAL_LIST_TYPE = 0;
    public static final int DONATION_STRESS_LIST_TYPE = 1;


    private int type = DONATION_INTERGRAL_LIST_TYPE;

    public V3MoneyDealingsListAdapter(List<T> data) {
        super(data);
        addItemType(V3MoneyListBaseData.LIST_CONTENT, R.layout.item_v3_money_dealings);
        addItemType(V3MoneyListBaseData.LIST_TITLE, R.layout.item_money_list_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (item instanceof V3MoneyDealingsEntity.DataBean) {
            helper.setText(R.id.tv_detail, ((V3MoneyDealingsEntity.DataBean) item).content);
            setItemEntityData(helper,item);
            helper.setText(R.id.tv_date, DateUtils.timeStampToStrMD(((V3MoneyDealingsEntity.DataBean) item).time));
        } else if (item instanceof V3MoneyDealingsEntity.StatisticsBean) {
            helper.getView(R.id.tv_month).setVisibility(View.GONE);
            helper.setTextColor(R.id.tv_income_expend, Color.BLACK);
            setItemMonthData(helper, item);
        }
    }

    private void setItemEntityData(BaseViewHolder helper, T item) {
        String amount = MoneyUtil.getLeXiangBiNoZero(((V3MoneyDealingsEntity.DataBean) item).amount);
        Double amountDouble = Double.valueOf(amount);
        if (type == DONATION_STRESS_LIST_TYPE) {
            amount = decimalFormat.format(amountDouble);
        }
        if (((V3MoneyDealingsEntity.DataBean) item).type < 100) {
            helper.setText(R.id.tv_amount, "+" +amount);
        } else {
            helper.setText(R.id.tv_amount, "-" + amount);
        }
    }


    private void setItemMonthData(BaseViewHolder helper, T item) {
        String income = MoneyUtil.getLeXiangBiNoZero(((V3MoneyDealingsEntity.StatisticsBean) item).income);
        String expend = MoneyUtil.getLeXiangBiNoZero(((V3MoneyDealingsEntity.StatisticsBean) item).expend);
        Double incomeDouble = Double.valueOf(income);
        Double expendDouble = Double.valueOf(expend);
        if (type == DONATION_STRESS_LIST_TYPE) {
            income = decimalFormat.format(incomeDouble);
            expend = decimalFormat.format(expendDouble);
        }
        helper.setText(R.id.tv_income_expend, ((V3MoneyDealingsEntity.StatisticsBean) item).date.split("-")[1] + "月  "
                + "收入: " + income
                + " 支出: " + expend);
    }

    public void setType(int type) {
        this.type = type;
    }
}

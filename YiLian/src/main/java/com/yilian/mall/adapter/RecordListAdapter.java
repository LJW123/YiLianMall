package com.yilian.mall.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.yilian.mall.R;
import com.yilian.mall.ui.RecordListActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.ExchangeTicketRecordEntity;

/**
 * 记录明细
 *
 * @author Created by Zg on 2017/12/13.
 */

public class RecordListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;
    /**
     * 收入支出分界 大于100是支出 小于100是收入
     */
    public static final int INT_100 = 100;

    public RecordListAdapter() {
        super(null);
        addItemType(RecordListRetention.EXCHANGE_MINE, R.layout.item_record_list);
        addItemType(RecordListRetention.EXCHANGE_VERIFICATION, R.layout.item_record_list);
        addItemType(RecordListRetention.WAIT_EXTRACT_LE_ANGEL, R.layout.item_record_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        //顶部统计
        headerTotal(helper, item);
        //内容
        content(helper, item);
    }

    /**
     * 顶部统计
     *
     * @param helper
     * @param item
     */
    private void headerTotal(BaseViewHolder helper, MultiItemEntity item) {
        LinearLayout llStickyHeader = helper.getView(R.id.ll_sticky_header);
        //月时间
        TextView tvStickyDate = helper.getView(R.id.tv_sticky_date);
        //统计
        TextView tvStickyTotal = helper.getView(R.id.tv_sticky_total);

        switch (item.getItemType()) {
            case RecordListRetention.EXCHANGE_MINE:
            case RecordListRetention.EXCHANGE_VERIFICATION:
                ExchangeTicketRecordEntity.ListBean mData = (ExchangeTicketRecordEntity.ListBean) item;
                if (helper.getLayoutPosition() == 0) {
                    llStickyHeader.setVisibility(View.VISIBLE);
                    String[] str = mData.date.split("-");
                    tvStickyDate.setText(str[0] + "年" + str[1] + "月");
                    tvStickyTotal.setText(RecordListActivity.statisticsIncome + MoneyUtil.getLeXiangBiNoZero(mData.income) + "  " + RecordListActivity.statisticsExpend + MoneyUtil.getLeXiangBiNoZero(mData.expend));
                    helper.itemView.setTag(FIRST_STICKY_VIEW);
                } else {
                    ExchangeTicketRecordEntity.ListBean previousData = (ExchangeTicketRecordEntity.ListBean) this.getItem(helper.getLayoutPosition() - 1);
                    if (!TextUtils.equals(mData.date, previousData.date)) {
                        llStickyHeader.setVisibility(View.VISIBLE);
                        String[] str = mData.date.split("-");
                        tvStickyDate.setText(str[0] + "年" + str[1] + "月");
                        tvStickyTotal.setText(RecordListActivity.statisticsIncome + MoneyUtil.getLeXiangBiNoZero(mData.income) + "  " + RecordListActivity.statisticsExpend + MoneyUtil.getLeXiangBiNoZero(mData.expend));
                        helper.itemView.setTag(HAS_STICKY_VIEW);
                    } else {
                        llStickyHeader.setVisibility(View.GONE);
                        helper.itemView.setTag(NONE_STICKY_VIEW);
                    }
                }
                helper.itemView.setContentDescription(new Gson().toJson(item));
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                mData = (ExchangeTicketRecordEntity.ListBean) item;
                if (helper.getLayoutPosition() == 0) {
                    llStickyHeader.setVisibility(View.VISIBLE);
                    String[] str = mData.date.split("-");
                    tvStickyDate.setText(str[0] + "年" + str[1] + "月");
                    tvStickyTotal.setText(RecordListActivity.statisticsIncome + mData.income + "  " + RecordListActivity.statisticsExpend + mData.expend);
                    helper.itemView.setTag(FIRST_STICKY_VIEW);
                } else {
                    ExchangeTicketRecordEntity.ListBean previousData = (ExchangeTicketRecordEntity.ListBean) this.getItem(helper.getLayoutPosition() - 1);
                    if (!TextUtils.equals(mData.date, previousData.date)) {
                        llStickyHeader.setVisibility(View.VISIBLE);
                        String[] str = mData.date.split("-");
                        tvStickyDate.setText(str[0] + "年" + str[1] + "月");
                        tvStickyTotal.setText(RecordListActivity.statisticsIncome + mData.income + "  " + RecordListActivity.statisticsExpend + mData.expend);
                        helper.itemView.setTag(HAS_STICKY_VIEW);
                    } else {
                        llStickyHeader.setVisibility(View.GONE);
                        helper.itemView.setTag(NONE_STICKY_VIEW);
                    }
                }
                helper.itemView.setContentDescription(new Gson().toJson(item));
                break;
            default:
                break;
        }


    }

    /**
     * 内容
     *
     * @param helper
     * @param item
     */
    private void content(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case RecordListRetention.EXCHANGE_MINE:
            case RecordListRetention.EXCHANGE_VERIFICATION:
                ExchangeTicketRecordEntity.ListBean mData = (ExchangeTicketRecordEntity.ListBean) item;
                GlideUtil.showCirIconNoSuffix(mContext, mData.img, helper.getView(R.id.iv_icon));
                helper.setText(R.id.tv_detail, mData.typeMsg);
                helper.setText(R.id.tv_day, DateUtils.timeStampToMDHM(mData.time));
                // 变化金额
                if (mData.type < INT_100) {
                    helper.setText(R.id.tv_amount, "+" + MoneyUtil.getLeXiangBiNoZero(mData.amount));
                } else {
                    helper.setText(R.id.tv_amount, "-" + MoneyUtil.getLeXiangBiNoZero(mData.amount));
                }
                break;
            case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                mData = (ExchangeTicketRecordEntity.ListBean) item;
                GlideUtil.showCirIconNoSuffix(mContext, mData.img, helper.getView(R.id.iv_icon));
                helper.setText(R.id.tv_detail, mData.typeMsg);
                helper.setText(R.id.tv_day, DateUtils.timeStampToMDHM(mData.time));
                // 变化金额
                if (mData.type < INT_100) {
                    helper.setText(R.id.tv_amount, "+" + mData.amount);
                } else {
                    helper.setText(R.id.tv_amount, "-" + mData.amount);
                }
                break;
            default:
                break;
        }
    }
}

package com.yilian.mall.utils;


import android.content.Context;
import android.content.Intent;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yilian.mall.ui.ExchangeTicketActivity;
import com.yilian.mall.ui.RecordDetailsActivity;
import com.yilian.mall.ui.RecordListActivity;
import com.yilian.mall.ui.V3MoneyDealingsActivity;
import com.yilian.mall.ui.transfer.TransferAccountActivity;
import com.yilian.mall.ui.transfer.TransferActivity;
import com.yilian.mall.ui.transfer.TransferSuccessActivity;
import com.yilian.mall.ui.transfer.impl.TransferDaiGouQUanCategoryImpl;
import com.yilian.mall.ui.transfer.inter.ITransfer;
import com.yilian.mall.ui.transfer.inter.ITransferAccount;
import com.yilian.mylibrary.utils.RecordListRetention;
import com.yilian.networkingmodule.entity.ExchangeTicketRecordEntity;

/**
 * 用于 益联益家 activity跳转等
 * <p>
 * Created by Zg on 2018/7/21
 */
public class JumpYlActivityUtils extends IsLoginUtils {

    /**
     * 兑换券
     *
     * @param mContext
     */
    public static void toExchangeTicket(Context mContext) {
        if (isLogin(mContext)) {
            Intent intent = new Intent(mContext, ExchangeTicketActivity.class);
            mContext.startActivity(intent);
        }
    }

    /**
     * 明细
     *
     * @param mContext
     */
    public static void toRecordList(Context mContext, @RecordListRetention.Record int recordType) {
        if (isLogin(mContext)) {
            Intent intent = new Intent(mContext, RecordListActivity.class);
            intent.putExtra("recordType", recordType);
            mContext.startActivity(intent);
        }
    }

    /**
     * 兑换明细详情
     *
     * @param mContext
     */
    public static void toRecordDetails(Context mContext, @RecordListRetention.Record int recordType, MultiItemEntity multiItemEntity) {
        if (isLogin(mContext)) {
            Intent intent = new Intent(mContext, RecordDetailsActivity.class);
            intent.putExtra("recordType", recordType);
            switch (recordType) {
                case RecordListRetention.EXCHANGE_MINE:
                case RecordListRetention.WAIT_EXTRACT_LE_ANGEL:
                    //兑换券明细列表 详情
                    ExchangeTicketRecordEntity.ListBean mData = (ExchangeTicketRecordEntity.ListBean) multiItemEntity;
                    intent.putExtra("data", mData);
                    break;
                default:
                    break;
            }
            mContext.startActivity(intent);
        }
    }

    /**
     * 跳转输入转赠账号界面
     *
     * @param context
     * @param iTransferAccount
     */
    public static void toTransferAccountActivity(Context context, ITransferAccount iTransferAccount, TransferDaiGouQUanCategoryImpl transferCategory) {
        Intent intent = new Intent(context, TransferAccountActivity.class);
        intent.putExtra(TransferAccountActivity.TAG, iTransferAccount);
        intent.putExtra(TransferAccountActivity.TAG_TRANSFER, transferCategory);
        context.startActivity(intent);
    }

    /**
     * 跳转转赠界面
     *
     * @param context
     * @param iTransfer
     */
    public static void toTransferActivity(Context context, ITransfer iTransfer) {
        Intent intent = new Intent(context, TransferActivity.class);
        intent.putExtra(TransferActivity.TAG, iTransfer);
        context.startActivity(intent);
    }

    public static void toTransferSuccessActivity(Context context, String content, String amount, String title) {
        Intent intent = new Intent(context, TransferSuccessActivity.class);
        intent.putExtra(TransferSuccessActivity.TAG, content);
        intent.putExtra(TransferSuccessActivity.TAG_TITLE, title);
        intent.putExtra(TransferSuccessActivity.TAG_AMOUNT, amount);
        context.startActivity(intent);
    }

    public static void toV3MoneyDealingsActivity(Context context, String phone, String titleName,
                                                 int type, String userId,String from, int donationListType) {

        Intent intent = new Intent(context, V3MoneyDealingsActivity.class);
        intent.putExtra("titlePhone", phone);
        intent.putExtra("titleName", titleName);
        intent.putExtra("type", type);
        intent.putExtra("id", userId);
        intent.putExtra("from", from);
        intent.putExtra("donation_list_type", donationListType);
        context.startActivity(intent);
    }
}


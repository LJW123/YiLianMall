package com.leshan.ylyj.adapter;


import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.CardMyBankCardsListEntity;

/**
 *
 * @author Administrator
 * @date 2017/12/20 0020
 */

public class MyBankCardsAdapter extends BaseQuickAdapter<CardMyBankCardsListEntity.DataBean, BaseViewHolder> {

    public MyBankCardsAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardMyBankCardsListEntity.DataBean item) {
        if (item.accountType==0){
            helper.setBackgroundRes(R.id.back_ll, R.drawable.my_bank_change_back);
            helper.setText(R.id.tv_card_type, null);
        }else {
            helper.setBackgroundRes(R.id.back_ll, R.drawable.my_bank_change_one_back);
            helper.setText(R.id.tv_card_type, "对公卡");
        }
        ImageView iv = helper.getView(R.id.bank_img);
        GlideUtil.showCirImage(mContext, item.bankLogo, iv);
        helper.setText(R.id.bank_tv, item.cardBank);
        helper.setText(R.id.cardtype_tv, item.cardType.equals("0") ? "储蓄卡" : "信用卡");
        String cardNum = item.cardNumber;
        helper.setText(R.id.cardnumber_tv, cardNum);
    }
}

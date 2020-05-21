package com.yilian.mall.ui.transfer.impl;

import android.content.Context;

import com.yilian.mall.ui.transfer.Donee;
import com.yilian.mall.ui.transfer.bean.TransferSuccessBean;
import com.yilian.mall.ui.transfer.inter.ITransfer;
import com.yilian.networkingmodule.entity.TransferIntegralEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author xiaofo on 2018/10/11.
 */

public class TransferDaiGouQuanImpl implements ITransfer {
    private final Donee donee;
    private final TransferDaiGouQUanCategoryImpl transferCategory;

    public TransferDaiGouQuanImpl(Donee donee, TransferDaiGouQUanCategoryImpl transferCategory) {
        this.donee = donee;
        this.transferCategory = transferCategory;
    }

    @Override
    public Donee getDonee() {
        return donee;
    }

    @Override
    public TransferDaiGouQUanCategoryImpl getTransferCategory() {
        return transferCategory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Observable<TransferSuccessBean> transfer(Context context, String transferAmount, String password, String remark) {
        return RetrofitUtils3.getRetrofitService(context)
                .transfer("quan/transfer_quan",
                        donee.phone, transferAmount, "0",
                        password, remark)
                .map(new Func1<TransferIntegralEntity,TransferSuccessBean>() {
                    @Override
                    public TransferSuccessBean call(TransferIntegralEntity transferIntegralEntity) {
                        TransferSuccessBean transferSuccessBean = new TransferSuccessBean();
                        transferSuccessBean.successAmount = transferIntegralEntity.data.amount;
                        transferSuccessBean.fee = transferIntegralEntity.data.fee;
                        return transferSuccessBean;
                    }
                })
                ;
    }



}

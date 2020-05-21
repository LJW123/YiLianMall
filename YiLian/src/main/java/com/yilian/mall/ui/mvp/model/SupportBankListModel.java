package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.entity.SupportBankListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/1/21.
 */

public class SupportBankListModel implements ISupportBankListModel {
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getSupportBankList(Context context, int type, Observer<SupportBankListEntity> observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .getSupportBankList("userBank/support_bank_list", type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }
}

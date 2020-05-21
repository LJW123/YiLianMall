package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/1/18.
 */

public class SmsCodeModelImpl implements ISmsCodeModel {
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getSmsCode(Context context, String phone, int verifyType, int type, String voice, String verifyCode, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .getSmsCodeV2("userBank/get_SMS_code_v2", phone, verifyType, type, voice, verifyCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription isShowImgCode(Context context, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .getShowImgCode("is_pop_verify_code")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription checkSmsCode(Context context, String phone, String smsCode, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .checkSmsCode("userBank/verify_SMS_code", phone, smsCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

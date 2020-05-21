package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/1/15.
 */

public class EditUserNameModelImpl implements IEditUserNameModel {
    @SuppressWarnings("unchecked")
    @Override
    public Subscription saveUserName(Context context, String data, Observer observer) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).setNickName("jfb/set_user_name", data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return subscription;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription saveStateOfMind(Context context, String mind, Observer observer) {

        Subscription subscription = RetrofitUtils3.getRetrofitService(context).setStateOfMind("jfb/set_personal_signature", mind)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return subscription;
    }
}

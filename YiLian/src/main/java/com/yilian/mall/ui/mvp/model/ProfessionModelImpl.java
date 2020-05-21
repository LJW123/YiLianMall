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

public class ProfessionModelImpl implements IProfessionModel {

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getProfessionData(Context context, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context).getProfessionData("jfb/getProfessionList")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

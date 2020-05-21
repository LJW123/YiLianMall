package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/2/12.
 */

public class LeDouHomePageModelImpl implements ILeDouHomePageModel {
    public static final int HOME_PAGE_LE_DOU = 0;
    public static final int SPECIAL_LE_DOU = 5;

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getData(Context context, int page, int beanType, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .getLeDouHomePageData("h_bean_list", page, Constants.PAGE_COUNT, beanType, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }
}

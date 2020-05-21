package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/1/16.
 */

public class LocationModelImpl implements ILocationModel {
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getAddressList(Context context, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context).getRegion("getRegion")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getLocation(Context context, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context)
                .getLocation("location/setLocation", PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, context)
                        , PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
                ;
    }
}

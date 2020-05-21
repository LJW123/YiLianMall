package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import com.yilian.networkingmodule.entity.BasicInformationEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Created by  on 2018/1/15.
 */

public class BasicInformationModelImpl implements IBasicInformationModel {
    @SuppressWarnings("unchecked")
    @Override
    public Subscription getData(Context context, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context).getBasicInformation("jfb/get_user_basic_info")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription setData(Context context, BasicInformationEntity.DataBean dataBean, Observer observer) {
        return RetrofitUtils3.getRetrofitService(context).setBasicInformation("jfb/set_user_basic_info",
                dataBean.sex, dataBean.age, dataBean.birthday, dataBean.bloodType, dataBean.school,
                dataBean.company, dataBean.profession, dataBean.professionName, dataBean.homeProvice, dataBean.homeCity, dataBean.homeCounty,
                dataBean.currentProvince, dataBean.currentCity, dataBean.currentCounty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
//        return null;
    }
}

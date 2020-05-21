package com.leshan.ylyj.model;

import android.content.Context;


import com.leshan.ylyj.basemodel.BaseModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class IntegralTreasureModel extends BaseModel {

    public IntegralTreasureModel(Context mContex) {
        super(mContex);
    }

    public Observable<rxfamily.entity.IntegralTreasureEntity> getIntegralTreasureData() {
        Observable<rxfamily.entity.IntegralTreasureEntity> observable = service.getIntegralTreasureData("jfb/integral_treasure_index")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<rxfamily.entity.BaseEntity> setRetainedIntegra(String type, String amount) {
        Observable<rxfamily.entity.BaseEntity> observable = service.setRetainedIntegra("jfb/set_auto_transfer", type, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<rxfamily.entity.IntegralRecordEntity> getRecord(String page, String count, String date, String type) {
        Observable<rxfamily.entity.IntegralRecordEntity> observable = service.getRecord("jfb/daily_details_list", page, count, date, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<rxfamily.entity.IntegralExchangeRecordEntity> getExchangeRecord(String page, String count, String date) {
        Observable<rxfamily.entity.IntegralExchangeRecordEntity> observable = service.getExchangeRecord("jfb/exchange_details_list", page, count, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<rxfamily.entity.IntegralStatusEntity> setShiftTo(String amount) {
        Observable<rxfamily.entity.IntegralStatusEntity> observable = service.setShiftTo("jfb/turn_in_integral", amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<rxfamily.entity.IntegralStatusEntity> setTurnOut(String amount) {
        Observable<rxfamily.entity.IntegralStatusEntity> observable = service.setTurnOut("jfb/turn_out_integral", amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    public Observable<rxfamily.entity.ExchangeIndexRecordEntity> getExchangeIndexRecord() {
        Observable<rxfamily.entity.ExchangeIndexRecordEntity> observable = service.getExchangeIndexRecord("jfb/user_integral_index")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}

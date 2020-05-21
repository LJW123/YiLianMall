package com.leshan.ylyj.model;

import android.content.Context;

import com.leshan.ylyj.basemodel.BaseModel;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class TreasureModel extends BaseModel {

    public TreasureModel(Context mContex) {
        super(mContex);
    }

    public TreasureModel(Context mContext, boolean has_cache) {
        super(mContext, has_cache);
    }

    public Observable<rxfamily.entity.MyBalanceEntity> getMoneyData() {
        Observable<rxfamily.entity.MyBalanceEntity> observable = service.getMoneyData("get_integral_balance")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

}

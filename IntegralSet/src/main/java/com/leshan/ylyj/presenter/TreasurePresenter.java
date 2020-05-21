package com.leshan.ylyj.presenter;

import android.content.Context;

import com.leshan.ylyj.base.BasePresenter;
import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.TreasureModel;

import rx.Subscription;

/**
 * 多个model的时候
 * 该类主要处理mV之间的交互逻辑
 * Created by Administrator on 2017/12/6 0006.
 */

public class TreasurePresenter extends BasePresenter {

    private TreasureModel mmodel;//定义传递参数

    public TreasurePresenter(Context context, BaseView view) {
        attachView(view);
        mmodel = new TreasureModel(context);
    }


    public void getMoneyData() {
        Subscription subscription = mmodel.getMoneyData().subscribe(observer);
        addSubscription(subscription);
    }


}

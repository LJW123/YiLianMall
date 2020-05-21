package com.leshan.ylyj.presenter;

import android.content.Context;

import com.leshan.ylyj.base.BasePresenter;
import com.leshan.ylyj.base.BaseView;
import com.leshan.ylyj.model.IntegralTreasureModel;
import com.leshan.ylyj.model.TreasureModel;

import rx.Subscription;

/**
 * 集分宝模块present
 */
public class IntegralTreasurePresenter extends BasePresenter {

    private IntegralTreasureModel mmodel;//定义传递参数

    public IntegralTreasurePresenter(Context context, BaseView view) {
        attachView(view);
        mmodel = new IntegralTreasureModel(context);
    }


    /**
     * 集分宝首页数据
     */
    public void getIntegralTreasureData() {
        Subscription subscription = mmodel.getIntegralTreasureData().subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 设置自动转入
     */
    public void setRetainedIntegra(String type, String amount) {
        Subscription subscription = mmodel.setRetainedIntegra(type, amount).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 明细
     */
    public void getRecord(int page, int count, String date, int type) {
        Subscription subscription = mmodel.getRecord(String.valueOf(page), String.valueOf(count), date, String.valueOf(type)).subscribe(observer);
        addSubscription(subscription);
    }
    /**
     * 累计兑换明细记录
     */
    public void getExchangeRecord(int page, int count, String date) {
        Subscription subscription = mmodel.getExchangeRecord(String.valueOf(page), String.valueOf(count), date).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 转入
     */
    public void setShiftTo(String amount) {
        Subscription subscription = mmodel.setShiftTo(amount).subscribe(observer);
        addSubscription(subscription);
    }


    /**
     * 转出
     */
    public void setTurnOut(String amount) {
        Subscription subscription = mmodel.setTurnOut(amount).subscribe(observer);
        addSubscription(subscription);
    }

    /**
     * 近7日兑换指数
     */
    public void getExchangeIndexRecord() {
        Subscription subscription = mmodel.getExchangeIndexRecord().subscribe(observer);
        addSubscription(subscription);
    }

}

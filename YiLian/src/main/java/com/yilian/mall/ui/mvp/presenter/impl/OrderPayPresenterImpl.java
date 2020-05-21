package com.yilian.mall.ui.mvp.presenter.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.impl.OrderPayModelImpl;
import com.yilian.mall.ui.mvp.presenter.inter.IOrderPayPresenter;
import com.yilian.mall.ui.mvp.view.inter.IOrderPayView;
import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.JsPayClass;
import com.yilian.networkingmodule.entity.GoodsChargeForPayResultEntity;
import com.yilian.networkingmodule.entity.PayOkEntity;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author xiaofo on 2018/7/8.
 */

public class OrderPayPresenterImpl implements IOrderPayPresenter {
    /**
     * 支付宝支付结果获取失败后,重新获取轮询次数
     */
    public static final int LOOP_TIMES = 5;
    public int initTime = 0;
    private final IOrderPayView mOrderPayView;
    private final OrderPayModelImpl orderPayModel;

    public OrderPayPresenterImpl(IOrderPayView orderPayView) {
        mOrderPayView = orderPayView;
        orderPayModel = new OrderPayModelImpl();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription cashPay(Context context, String orderId, String payPwd, String merchantIncome) {
        return orderPayModel.payCash(context, orderId, payPwd, merchantIncome)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PayOkEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mOrderPayView.payFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(PayOkEntity payOkEntity) {
                        mOrderPayView.cashPaySuccess(payOkEntity);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription createThirdPayOrder(Context context, String payType, String paymentFee,
                                            String consumerOpenId, String type, String orderId, String merchantIncome) {
        return orderPayModel.createThirdPayOrder(context, payType, paymentFee,
                consumerOpenId, type, orderId, merchantIncome)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsPayClass>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JsPayClass jsPayClass) {
                        mOrderPayView.createGoodsPrepareThirdPayOrder(jsPayClass);
                    }
                })
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription getPayResult(Context context, String orderId, String type) {
        mOrderPayView.startMyDialog();
        return orderPayModel.getPayResult(context, orderId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsChargeForPayResultEntity>() {
                    @Override
                    public void onCompleted() {
                        mOrderPayView.stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mOrderPayView.stopMyDialog();
                        mOrderPayView.showToast(e.getMessage());
                        if (e instanceof CodeException) {
                            if (((CodeException) e).code == -100) {
                                if(initTime<LOOP_TIMES){
                                    getPayResult(context, orderId, type);
                                    initTime++;
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(GoodsChargeForPayResultEntity goodsChargeForPayResultEntity) {
                        mOrderPayView.getPayResultSuccess(goodsChargeForPayResultEntity);
                    }
                })
                ;
    }

    @Override
    public void onDestory() {

    }
}

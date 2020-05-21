package com.yilian.mall.ui.mvp.model.impl;

import android.content.Context;

import com.yilian.mall.ui.mvp.model.inter.IOrderPayModel;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/8.
 */

public class OrderPayModelImpl implements IOrderPayModel {
    @Override
    public Observable payCash(Context context, String orderId, String payPwd, String merchantIncome) {
        return RetrofitUtils3.getRetrofitService(context)
                .payCash("pay/pay_balance_v2", orderId, payPwd, merchantIncome)
                ;
    }

    @Override
    public Observable createThirdPayOrder(Context context, String payType,
                                          String paymentFee, String consumerOpenId,
                                          String type, String orderId, String merchantIncome) {
        return RetrofitUtils3.getRetrofitService(context)
                .createGoodsThirdPreparePayOrder("recharge/recharge_order", payType, paymentFee,
                        consumerOpenId, type, orderId, merchantIncome)
                ;
    }

    @Override
    public Observable getPayResult(Context context, String orderId, String type) {
        return RetrofitUtils3.getRetrofitService(context)
                .getPayResult("pay_info",orderId,type)
                ;
    }
}

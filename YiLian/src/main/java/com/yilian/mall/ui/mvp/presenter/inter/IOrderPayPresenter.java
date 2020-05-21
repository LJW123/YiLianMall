package com.yilian.mall.ui.mvp.presenter.inter;

import android.content.Context;

import com.yilian.mall.ui.mvp.presenter.IBasePresenter;

import rx.Subscription;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IOrderPayPresenter extends IBasePresenter {
    /**
     * 支付
     *
     * @param context
     * @param orderId        支付订单id
     * @param payPwd         支付密码 无第三方支付时需要该字段
     * @param merchantIncome 商家营收支付金额
     * @return
     */
    Subscription cashPay(Context context, String orderId, String payPwd,
                     String merchantIncome);

    /**
     * 第三方支付生成预支付订单
     *
     * @param context
     * @param payType
     * @param paymentFee
     * @param consumerOpenId
     * @param type
     * @param orderId
     * @param merchantIncome
     * @return
     */
    Subscription createThirdPayOrder(Context context, String payType,
                                     String paymentFee, String consumerOpenId,
                                     String type, String orderId, String merchantIncome);

    /**
     * 获取支付结果
     * @param orderId 支付订单号
     * @param type 商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    Subscription getPayResult(Context context,String orderId,String type);
}

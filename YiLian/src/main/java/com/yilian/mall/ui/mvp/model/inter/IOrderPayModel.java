package com.yilian.mall.ui.mvp.model.inter;


import android.content.Context;

import com.yilian.mall.ui.mvp.model.IBaseModel;

import rx.Observable;

/**
 * @author xiaofo on 2018/7/8.
 */

public interface IOrderPayModel extends IBaseModel {
    /**
     * 无第三方支付方式进行支付
     *
     * @param context
     * @param orderId        支付订单号
     * @param payPwd         支付密码
     * @param merchantIncome 商家营收金额
     * @return
     */
    Observable payCash(Context context, String orderId, String payPwd, String merchantIncome);

    /**
     * 第三方支付之前 生成预支付订单
     *
     * @param context
     * @param payType        //1支付宝 2微信 3微信公共账号 4网银
     * @param paymentFee     //支付总价 单位元
     * @param consumerOpenId //微信唯一id app情况传0
     * @param type           //0正常充值 1乐享币直充
     * @param orderId        订单号
     * @param merchantIncome 商家营收款 单位分
     * @return
     */
    Observable createThirdPayOrder(Context context, String payType, String paymentFee, String consumerOpenId,
                                   String type, String orderId, String merchantIncome);

    /**
     * 获取支付结果
     * @param context
     * @param orderId 订单号
     * @param type 商品订单1 商家入驻缴费2 扫码支付3 线下交易4 京东订单 11
     * @return
     */
    Observable getPayResult(Context context,String orderId,String type);
}

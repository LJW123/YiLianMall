package com.yilian.mylibrary.pay;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xiaofo on 2018/7/19.
 */

public class ThirdPayForType {
    /**
     * 商城订单支付
     */
    public static final String PAY_FOR_GOODS = "1";
    /**
     * 商家年费缴纳及续费
     */
    public static final String PAY_FOR_ANNUAL_FEE = "2";
    /**
     * 商家线下现金交易
     */
    public static final String PAY_FOR_OFF_LINE_PAY = "4";
    /**
     * 充值
     */
    public static final String PAY_FOR_RECHARGE = "5";
    /**
     * 店内消费/扫码支付
     */
    public static final String PAY_FOR_SCAN = "10";
    /**
     * 京东商品支付
     */
    public static final String PAY_FOR_JD_GOODS = "11";
    /**
     * 苏宁商品支付
     */
    public static final String PAY_FOR_SN_GOODS = "12";
    /**
     * 兑换券支付
     */
    public static final String PAY_FOR_EXCHANGE = "13";
    /**
     * 携程 支付
     */
    public static final String PAY_FOR_CTRIP = "14";

    /**
     * 京东购物卡订单 支付
     */
    public static final String PAY_FOR_JD_GOODS_CARD = "15";


    /**
     * 支付类型
     */
    @StringDef({PAY_FOR_GOODS, PAY_FOR_ANNUAL_FEE, PAY_FOR_OFF_LINE_PAY, PAY_FOR_RECHARGE, PAY_FOR_SCAN, PAY_FOR_JD_GOODS, PAY_FOR_SN_GOODS, PAY_FOR_CTRIP, PAY_FOR_JD_GOODS_CARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayType {
    }
}

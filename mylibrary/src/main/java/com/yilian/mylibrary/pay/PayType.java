package com.yilian.mylibrary.pay;

/**
 * @author xiaofo on 2018/7/8.
 */

public class   PayType {
    /**
     * 无第三方支付方式支付
     */
    public static final String  CASH_PAY= "-1";
    /**
     * 支付宝
     */
    public static final String ALI_PAY = "1";
    /**
     * 微信
     */
    public static final String WE_CHAT_PAY="2";
    /**
     * 微信公众号
     */
    public static final String WX_PUBLIC_PLATFORM_PAY="3";
    /**
     * 网银
     */
    public static final String ONLINE_BANK_PAY="4";

}

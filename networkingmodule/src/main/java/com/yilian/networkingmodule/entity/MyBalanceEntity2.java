package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by liuyuqi on 2017/6/8 0008.
 */
public class MyBalanceEntity2 extends HttpResultBean {
    @SerializedName("user_total_quan")
    public String exchange;
    @SerializedName("quan")
    public String diGouQuan;
    public static final int IS_MERCHANT = 1;
    public static final int NOT_MERCANT = 0;
    /**
     * 是否是商家 0不是商家 1是商家
     */
    @SerializedName("isMerchant")
    public int isMerchant;
    /**
     * 商家营收金额
     */
    @SerializedName("money")
    public String merchantIncome;
    /**
     * PendIntegral : 0
     * available_lebi : 0
     * available_voucher : 0
     * balance : 0
     * cash : 0
     * coupon : 0
     * integral : 2000000
     * lebi : 0
     * voucher : 0
     */
    @SerializedName("yi_dou_url")
    public String yiDouUrl;
    /**
     * 待结算奖券
     */
    @SerializedName("PendIntegral")
    public String PendIntegral;
    @SerializedName("available_lebi")
    public int availableMoney;
    @SerializedName("available_voucher")
    public int availableVoucher;
    @SerializedName("balance")
    public String balance;
    @SerializedName("cash")
    public String cash;
    @SerializedName("coupon")
    public String coupon;
    /**
     * 奖券
     */
    @SerializedName("integral")
    public String integral;
    @SerializedName("usedIntegral")
    public String usedIntegral;

    @SerializedName("lebi")
    /**
     * 奖励金额
     */
    public String lebi;
    @SerializedName("voucher")
    public int voucher;

    @SerializedName("mallIntegral")
    public String mallIntegral;// 商城奖券   mallIntegral
    @SerializedName("integralNumber")
    public String integraNumber;//奖券指数  integralNumber
    @SerializedName("integralNumber_v1")
    public String integralNumberV1;
    @SerializedName("integralNumbers")
    public String integralNumbers;//带百分号的奖券指数
    @SerializedName("totalIntegralBonus")
    public String totalIntegralBonus;//累计奖券发奖励totalIntegralBonus
    @SerializedName("balanceNum")
    public String balanceNum;//累计领奖励   balanceNum
    @SerializedName("daily_cash")
    public String dailyCash;

}

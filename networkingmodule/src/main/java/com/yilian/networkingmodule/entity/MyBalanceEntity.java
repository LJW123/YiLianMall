package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by liuyuqi on 2017/6/8 0008.
 */
public class MyBalanceEntity extends HttpResultBean {


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
    /**
     * 奖励金额
     */
    @SerializedName("lebi")
    public double lebi;
    @SerializedName("voucher")
    public int voucher;

    @SerializedName("mallIntegral")
    public String mallIntegral;// 商城奖券   mallIntegral
    @SerializedName("integralNumber")
    public String integraNumber;//奖券指数  integralNumber
    @SerializedName("integralNumbers")
    public String integralNumbers;//带百分号的奖券指数
    @SerializedName("totalIntegralBonus")
    public String totalIntegralBonus;//累计奖券发奖励totalIntegralBonus
    @SerializedName("balanceNum")
    public String balanceNum;//累计领奖励   balanceNum
    @SerializedName("daily_cash")
    public String dailyCash;

    @SerializedName("yi_dou_bao")
    public String yi_dou_bao;
    @SerializedName("yi_dou_jump")
    public String yi_dou_jump;
    @SerializedName("yi_dou_url")
    public String yi_dou_url;
    @SerializedName("integralNumber_v1")
    public String integralNumber_v1;
    @SerializedName("integralNumbers_v1")
    public String integralNumbers_v1;

}

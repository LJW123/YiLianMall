package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuyuqi on 2017/6/13 0013.
 */
public class RechargeOrderEntity extends BaseEntity implements Serializable {
    @SerializedName("paymentIndex")//支付订单编号
    public String paymentIndex;
    @SerializedName("payment_apply_time")//请求发起时间
    public String paymentApplyTime;
    @SerializedName("payment_fee")//充值金额
    public String paymentFee;
    @SerializedName("orderString")
    public String orderString;
}

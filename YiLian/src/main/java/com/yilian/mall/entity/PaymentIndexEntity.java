package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/3/31.
 */
public class PaymentIndexEntity extends BaseEntity {
    @SerializedName("paymentIndex")//支付订单编号
    public String paymentIndex;
    @SerializedName("payment_apply_time")//请求发起时间
    public String paymentApplyTime;
    @SerializedName("payment_fee")//充值金额 单位元
    public String paymentFee;
    @SerializedName("orderString")
    public String orderString;
}

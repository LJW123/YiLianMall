package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/6/13 0013.
 */
public class MerchantCashPayEntity extends BaseEntity{

    /**
     * merchant_id :
     * order_id :
     */

    @SerializedName("merchant_id")
    public String merchantId;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("status")
    public String status;//该笔支付是缴费还是续费 0缴费 1续费
    @SerializedName("merchant_type")
    public String merchantType;//当前商家类型 0 个体 1 实体
}

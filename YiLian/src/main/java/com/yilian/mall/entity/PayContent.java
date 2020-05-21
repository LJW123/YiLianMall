package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yukun on 2016/4/19.
 */
public class PayContent extends BaseEntity{

    @SerializedName("expend_lebi")
    public String expendLebi; // 支付乐币

    @SerializedName("expend_lefen")
    public String expendLefen; // 支付乐分

    @SerializedName("expend_coupon")
    public String expendCoupon; // 支付券

    public String name; // 商家名字
}

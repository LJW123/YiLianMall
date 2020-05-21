package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/9.
 */
public class ChargeDisplayEntity extends BaseEntity {
    @SerializedName("wechatpaytype")
    public String wechatpaytype;
    @SerializedName("alipaytype")
    public String alipaytype;
}

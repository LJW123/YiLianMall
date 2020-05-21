package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ChargeSuccessEntity extends BaseEntity {

    @SerializedName("recharge_lebi")
    public float recharge_lebi;
    @SerializedName("lev1")//普通会员所需金额
    public float lev1;
    @SerializedName("lev2")//VIP会员所需金额
    public float lev2;
    @SerializedName("lev3")//至尊会员所需金额
    public float lev3;
}

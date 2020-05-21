package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/3/31.
 */
public class TiXianEntity extends BaseEntity {

    @SerializedName("coupon")//乐券奖励
    public double coupon;
    @SerializedName("lebi")//乐币奖励
    public double lebi;
    @SerializedName("lefen")//乐发奖励包
    public String lefen;
    @SerializedName("available_lebi")//用户可领奖励数量
    public String availableLebi;

}

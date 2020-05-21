package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/28.
 */
public class RollOutSuccessEntity extends BaseEntity {

    @SerializedName("lebi")//转账时间
    public String lebi;
    @SerializedName("time")//转账数量
    public String time;
    @SerializedName("avible_lebi")//剩余可转金额
    public String avible_lebi;

}

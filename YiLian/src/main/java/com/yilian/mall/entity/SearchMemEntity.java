package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SearchMemEntity extends BaseEntity {
    @SerializedName("member_lev")//会员等级
    public String member_lev;
    @SerializedName("member_income")//为我带来领奖励
    public String member_income;
    @SerializedName("member_buy_lefen")//会员消费乐分币
    public String member_buy_lefen;
    @SerializedName("member_buy_lebi")//会员消费乐享币
    public String member_buy_lebi;
    @SerializedName("member_name")//会员名称
    public String member_name;
    @SerializedName("member_icon")//用户头像
    public String member_icon;
    @SerializedName("reg_time")//会员注册时间
    public long reg_time;
}

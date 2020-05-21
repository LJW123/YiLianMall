package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MemberGiftEntity extends BaseEntity {
    @SerializedName("name")
    public String name; // 转赠对象的昵称
    @SerializedName("photo")
    public String photo; // 转赠对象头像
    @SerializedName("availableMoney")
    public String availableLebi;
    @SerializedName("availableLefen")
    public String availableLefen;
    @SerializedName("poundagePercent")
    public String poundagePercent;
    @SerializedName("availableIntegral")
    public String avilableIntegral;//奖券

    public int lev; // 会员等级 1 普通会员  2 vip会员 3 至尊会员

}

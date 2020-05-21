package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lefenandroid on 16/7/9.
 */
public class MainFansIncomeEntity extends BaseEntity{

    /**
     * member_lev : 会员等级
     * member_count : 会员总人数
     * member_income : 返利总领奖励
     * lefen_member_count : 益联益家总人数
     * lefenbao : 累计领奖励
     */

    @SerializedName("member_lev")
    public String memberLev;
    @SerializedName("member_count")
    public String memberCount;
    @SerializedName("member_income")
    public String memberIncome;
    @SerializedName("lefen_member_count")
    public String lefenMemberCount;
    @SerializedName("lefenbao")
    public String lefenbao;

}

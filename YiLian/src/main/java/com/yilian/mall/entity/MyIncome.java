package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/3/28.
 */
public class MyIncome extends BaseEntity {
    @SerializedName("member_lev")
    public String memberLev;
    @SerializedName("member_count")
    public String memberCount;
    @SerializedName("member_income")
    public String memberIncome;
    @SerializedName("lefen_member_count")
    public String fansNumber;

    @SerializedName("lev1")
     public MemberLev1 memberLev1;
    @SerializedName("lev2")
    public MemberLev2 memberLev2;
    @SerializedName("lev3")
    public MemberLev3 memberLev3;

    public class MemberLev1 {
        @SerializedName("member_count")
        public String memberCount;
        @SerializedName("member_income")
        public String memberIncome;
    }

    public class MemberLev2 {
        @SerializedName("member_count")
        public String memberCount;
        @SerializedName("member_income")
        public String memberIncome;
    }

    public class MemberLev3 {
        @SerializedName("member_count")
        public String memberCount;
        @SerializedName("member_income")
        public String memberIncome;
    }
}

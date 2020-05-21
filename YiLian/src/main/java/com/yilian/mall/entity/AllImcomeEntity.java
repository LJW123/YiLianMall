package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/16.
 */
public class AllImcomeEntity extends BaseEntity {

    public ArrayList<MemberIncome> list;

    public class MemberIncome{
        @SerializedName("member_lev")
        public String member_lev;
        @SerializedName("consumer_gain_lebi")
        public String member_income;
        @SerializedName("deal_time")
        public String deal_time;
    }
}

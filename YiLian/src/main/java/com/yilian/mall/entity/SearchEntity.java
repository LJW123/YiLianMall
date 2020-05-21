package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.MembersEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SearchEntity extends BaseEntity {
    @SerializedName("members")
    public ArrayList<MembersEntity> memList;

//    public class Members {
//        @SerializedName("member_id")
//        public String member_id;
//        @SerializedName("member_lev")
//        public String member_lev;
//        @SerializedName("member_income")
//        public String member_income;
//        @SerializedName("member_buy_lefen")
//        public String member_buy_lefen;
//        @SerializedName("member_buy_lebi")
//        public String member_buy_lebi;
//        @SerializedName("member_name")
//        public String member_name;
//        @SerializedName("reg_time")
//        public String reg_time;
//        @SerializedName("member_icon")
//        public String member_icon;
//    }

}

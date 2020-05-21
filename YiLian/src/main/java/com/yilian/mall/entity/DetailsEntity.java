package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.entity.MembersEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/18.
 */
public class DetailsEntity extends BaseEntity {

    @SerializedName("head")
    public String head;
    @SerializedName("username")
    public String userName;
    @SerializedName("reg_time")
    public String regTime;
    @SerializedName("lev")
    public String lev;
    @SerializedName("cash")
    public String cash;
    @SerializedName("phone")
    public String phone;

    @SerializedName("member_count")
    public String memberCount;
    @SerializedName("user_lev")
    public String userLev;
    @SerializedName("user_lev_name")
    public String userLevName;
    @SerializedName("fromRefrrer")
    public String fromRefrrer;
    @SerializedName("fromRefrrerId")
    public String fromRefrrerId;
    @SerializedName("members")
    public ArrayList<MembersEntity> memberList;

//    public static class MembersBean {
//        @SerializedName("member_icon")
//        public String memberIcon;
//        @SerializedName("member_id")
//        public String memberId;
//        @SerializedName("member_name")
//        public String memberName;
//        @SerializedName("reg_time")
//        public String regTime;
//        @SerializedName("rank")
//        public String rank;
//    }
}

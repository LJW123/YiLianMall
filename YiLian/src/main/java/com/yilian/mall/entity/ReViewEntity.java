package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 往期回顾列表中实体类
 * Created by Administrator on 2016/5/26.
 */
public class ReViewEntity extends BaseEntity {
    @SerializedName("snatch_info")
    public ArrayList<SnatchInfo> snatch_info;

    public class SnatchInfo{
        @SerializedName("activity_id")//活动id
        public String activity_id;
        @SerializedName("snatch_issue")//活动期号
        public String snatch_issue;
        @SerializedName("snatch_announce_time")//揭晓时间
        public String snatch_announce_time;
        @SerializedName("user_name")//用户昵称
        public String user_name;
        @SerializedName("phone")//手机号码
        public String phone;
        @SerializedName("win_num")//中奖号码
        public String win_num;
        @SerializedName("join_count")//活动参与次数
        public int join_count;
        @SerializedName("snatch_goods_url")
        public String snatch_goods_url;
        @SerializedName("photo")//用户头像
        public String user_photo;
    }
}

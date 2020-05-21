package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/27.
 */
public class NGotRecordEntity extends BaseEntity {
    @SerializedName("snatch_info")
    public ArrayList<SnatchInfo> snatch_info;
    public class SnatchInfo{
        @SerializedName("activity_id")//活动id
        public String activity_id;
        @SerializedName("snatch_issue")//活动期号
        public String snatch_issue;
        @SerializedName("snatch_announce_time")//揭晓时间
        public String snatch_announce_time;
        @SerializedName("snatch_name")//活动标题
        public String snatch_name;
        @SerializedName("goods_url")//图片
        public String goods_url;
        @SerializedName("win_num")//中奖号码
        public String win_num;
        @SerializedName("join_count")//活动参与次数
        public int join_count;
        @SerializedName("award_status")//奖励状态：0表示未开奖，1表示未设置收货地址，2待发货，3，已发货
        public int status;
        @SerializedName("user_name")//用户名
        public String user_name;
        @SerializedName("phone")//用户手机
        public String phone;
        @SerializedName("user_id")//用户user_id
        public String user_id;
        @SerializedName("zone")//判断1，10元区
        public String zone;

    }
}

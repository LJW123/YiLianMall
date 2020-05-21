package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MySnatchEntity extends BaseEntity {

    @SerializedName("list")
    public ArrayList<MySnatch> list;

    public class MySnatch {
        @SerializedName("activity_id")
        public String activity_id;
        @SerializedName("activity_name")
        public String activityName;
        @SerializedName("activity_status")
        public String activity_status;
        @SerializedName("activity_submit_time")
        public String activity_submit_time;
        @SerializedName("goods_name")
        public String goodsname;
        @SerializedName("join_count")
        public String join_count;
        @SerializedName("luck_number")
        public int luck_number;
        @SerializedName("open_time")
        public String open_time;
        @SerializedName("goods_icon")
        public String goods_icon;
        @SerializedName("status")
        public String status;
        @SerializedName("has_address")
        public int hasAddress;
    }
}

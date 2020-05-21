package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/9.
 */
public class SnatchDetailsEntity extends BaseEntity {

    @SerializedName("luck_number")
    public String luckNumber;
    @SerializedName("phone")
    public String phone;
    @SerializedName("join_count")
    public String joinCount;
    @SerializedName("join_time")
    public String joinTime;
    @SerializedName("log")
    public ArrayList<Log2> logList;

    public class Log2 {
        @SerializedName("luck_number")
        public String luckNumber;
        @SerializedName("join_count")
        public int joinCount;
    }
}
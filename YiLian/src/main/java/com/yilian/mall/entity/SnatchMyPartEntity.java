package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/9.
 */
public class SnatchMyPartEntity extends BaseEntity {
    /**
     * 我的记录
     */
    @SerializedName("status")
    public String status;
    @SerializedName("luck_number")
    public String luck_number;
    @SerializedName("log")
    public ArrayList<Log> logslist;

    public class Log{
        @SerializedName("luck_number")
        public String luck_number;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("join_time")
        public String joinTime;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
    }

}

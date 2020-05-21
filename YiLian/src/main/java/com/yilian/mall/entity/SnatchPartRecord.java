package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/6.
 */
public class SnatchPartRecord extends BaseEntity {
    /**
     * 夺宝详情夺宝数字详情
     */

    @SerializedName("log")
    public ArrayList<Log3> listLog;

    public class Log3 {
        @SerializedName("photo")
        public String photo;
        @SerializedName("phone")
        public String phone;
        @SerializedName("join_time")
        public String joinTime;
    }
}

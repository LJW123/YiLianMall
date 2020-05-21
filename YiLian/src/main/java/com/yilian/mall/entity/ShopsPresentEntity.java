package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * author LiXiuRen  PRAY NO BUG
 * Created by Administrator on 2016/7/2.
 */
public class ShopsPresentEntity extends BaseEntity{

    /**
     * send_time : 1462179501
     * send_count : 8
     * user_name :
     */

    @SerializedName("log")
    public ArrayList<LogBean> log;

    public static class LogBean {
        @SerializedName("send_time")
        public String sendTime;
        @SerializedName("send_count")
        public String sendCount;
        @SerializedName("user_name")
        public String userName;
    }
}

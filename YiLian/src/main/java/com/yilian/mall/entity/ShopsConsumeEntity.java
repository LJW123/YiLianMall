package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * author LiXiuRen  PRAY NO BUG
 * Created by Administrator on 2016/7/4.
 */
public class ShopsConsumeEntity extends BaseEntity {

    /**
     * deal_time : 1466826523
     * user_name : 力王
     * photo : http://wx.qlogo.cn/mmopen/5ebZqP0bp2CibzhWKydbLl1MxjwZEfI0nmdgKAnhFrO5qSmHR0L4V6pKgicE3FYxHAJXS6e0gw8sjGgFnSRgbSLhn8H5ibG40xia/0
     */

    @SerializedName("log")
    public ArrayList<LogBean> log;

    public static class LogBean {
        @SerializedName("deal_time")
        public String dealTime;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("photo")
        public String photo;
    }
}

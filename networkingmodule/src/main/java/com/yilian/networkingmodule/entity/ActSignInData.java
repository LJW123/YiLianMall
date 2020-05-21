package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/14 0014.
 */

public class ActSignInData extends HttpResultBean {
    /**
     * code: 1,
     * integral: 456546,    //用户当前奖券
     * days: 0,                  //连续签到天数
     * addIntegral: "0.00",    //今日签到增加奖券
     * isTodaySign: 0,         //今日是否签到，0未签到  1已签到
     * totalDay: 7              //签到周期，7天一个周期
     * datelist:                   //日期列表
     * curDate:                   //今天的日期
     * curMonth: 当前月份
     */

    @SerializedName("integral")
    public String integral;
    @SerializedName("days")
    public int days;
    @SerializedName("addIntegral")
    public String addIntegral;
    @SerializedName("isTodaySign")
    public int isTodaySign;
    @SerializedName("TotalDay")
    public int totalDay;
    @SerializedName("curDate")
    public String curDate;
    @SerializedName("curMonth")
    public String curMonth;
    @SerializedName("datelist")
    public List<DatelistBean> datelist;

    public static class DatelistBean {
        /**
         * days : 17
         * isday : 0
         */

        @SerializedName("days")
        public String days;
        @SerializedName("isday")
        public int isday;//0 不是当天  1 是当天
    }


}

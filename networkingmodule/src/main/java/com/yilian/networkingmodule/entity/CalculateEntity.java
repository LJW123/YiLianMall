package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by LYQ on 2017/10/31.
 */

public class CalculateEntity extends HttpResultBean{

    /**
     * code :
     * base_num :
     * res_time :
     * win_num :
     * snatch_log : [{"format_time":"","time_number":"","time":"","user_name":""},{}]
     */

    @SerializedName("base_num")
    public String baseNum;
    @SerializedName("res_time")
    public String resTime;
    @SerializedName("win_num")
    public String winNum;
    @SerializedName("snatch_log")
    public List<SnatchLogBean> snatchLog;

    public static class SnatchLogBean {
        /**
         * format_time :
         * time_number :
         * time :
         * user_name :
         */

        @SerializedName("format_time")
        public String formatTime;
        @SerializedName("time_number")
        public String timeNumber;
        @SerializedName("time")
        public String time;
        @SerializedName("user_name")
        public String userName;
    }
}

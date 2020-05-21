package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/10/26 0026.
 */

public class JPLocation extends BaseEntity{

    /**
     * activity_index : 197
     * activity_type : 1
     * activity_limit : 1
     * activity_mode : 0
     * sponsor_type : 0
     * activity_name : 123
     * activity_sponsor : 335
     * activity_sponsor_name : 运营礼包
     * activity_city : 149
     * activity_county : 1254
     * activity_status : 1
     * activity_start_time : 1463900580
     * activity_end_time : 0
     * activity_prize_url : app/icon_lefen.png
     * activity_total_count : 0
     * activity_play_count : 27
     * activity_expend : 1
     * update_time : 0
     * activity_server_time : 1477449516
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("activity_index")
        public String activityIndex;
        @SerializedName("activity_type")
        public String activityType;
        @SerializedName("activity_limit")
        public String activityLimit;
        @SerializedName("activity_mode")
        public String activityMode;
        @SerializedName("sponsor_type")
        public String sponsorType;
        @SerializedName("activity_name")
        public String activityName;
        @SerializedName("activity_sponsor")
        public String activitySponsor;
        @SerializedName("activity_sponsor_name")
        public String activitySponsorName;
        @SerializedName("activity_city")
        public String activityCity;
        @SerializedName("activity_county")
        public String activityCounty;
        @SerializedName("activity_status")
        public String activityStatus;
        @SerializedName("activity_start_time")
        public String activityStartTime;
        @SerializedName("activity_end_time")
        public String activityEndTime;
        @SerializedName("activity_prize_url")
        public String activityPrizeUrl;
        @SerializedName("activity_total_count")
        public String activityTotalCount;
        @SerializedName("activity_play_count")
        public String activityPlayCount;
        @SerializedName("activity_expend")
        public String activityExpend;
        @SerializedName("update_time")
        public String updateTime;
        @SerializedName("activity_server_time")
        public int activityServerTime;
    }
}

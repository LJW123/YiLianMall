package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/13.
 */
public class OneBuyFromGoingToAnnouncedEntity extends BaseEntity {

    /**
     * activity_id :
     * snatch_issue :
     * snatch_name :
     * snatch_subhead :
     * snatch_announce_time :
     * snatch_goods_url :
     * user_name :
     * phone :
     * photo :
     * win_num :
     * join_count :
     * total_count :
     */

    @SerializedName("snatch_info")
    public ArrayList<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean {
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_subhead")
        public String snatchSubhead;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("snatch_goods_url")
        public String snatchGoodsUrl;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("total_count")
        public String totalCount;
    }
}

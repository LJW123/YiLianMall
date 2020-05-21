package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/4 0004.
 */

public class LuckyAwardListEntity extends HttpResultBean {

    /**
     * code :
     * snatch_info : [{"activity_id":"","snatch_issue":"","snatch_name":"","snatch_subhead":"","snatch_announce_time":"","snatch_goods_url":"","user_name":"","phone":"","photo":"","win_num":"","join_count":"","total_count":""}]
     */

    @SerializedName("snatch_info")
    public List<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean {
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

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_subhead")
        public String snatchSubhead;
        @SerializedName("snatch_announce_time")
        public long snatchAnnounceTime;
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
        @SerializedName("extra_bean")
        public String extraBean;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("total_count")
        public String totalCount;
    }
}

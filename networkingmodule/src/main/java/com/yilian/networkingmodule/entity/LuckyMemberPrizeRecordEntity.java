package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/15 0015.
 */

public class LuckyMemberPrizeRecordEntity extends HttpResultBean {

    /**
     * code :
     * user_name :
     * phone :
     * photo :
     * list : [{"isSelf":"","activity_id":"","snatch_issue":"","snatch_name":"","goods_url":"","snatch_announce_time":"","win_num":"","join_count":"","total_count":"","mycount":"","award_status":"","user_name":"","phone":"","photo":""},{}]
     */

    @SerializedName("user_name")
    public String userName;
    @SerializedName("phone")
    public String phone;
    @SerializedName("photo")
    public String photo;
    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * isSelf :
         * activity_id :
         * snatch_issue :
         * snatch_name :
         * goods_url :
         * snatch_announce_time :
         * win_num :
         * join_count :
         * total_count :
         * mycount :
         * award_status :
         * user_name :
         * phone :
         * photo :
         */

        @SerializedName("isSelf")
        public String isSelf;
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("goods_url")
        public String goodsUrl;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("total_count")
        public String totalCount;
        @SerializedName("mycount")
        public String mycount;
        @SerializedName("award_status")
        public String awardStatus;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
    }
}

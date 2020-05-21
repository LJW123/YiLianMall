package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author LYQ Created by LYQ on 2017/10/28.
 */

public class SnatchAwardDetailsEntity extends HttpResultBean {

    /**
     * code :
     * snatch_info : {"activity_id":"","snatch_issue":"","snatch_announce_time":"","express_time":"","express_id":"","express_name":"","express_num":"","confirm_time":"","win_num":"","join_count":"","award_status":"","prize_goods_name":"","prize_goods_url":""}
     */

    @SerializedName("snatch_info")
    public SnatchInfoBean snatchInfo;

    public static class SnatchInfoBean {
        /**
         * activity_id :
         * snatch_issue :
         * snatch_announce_time :
         * express_time :
         * express_id :
         * express_name :
         * express_num :
         * confirm_time :
         * win_num :
         * join_count :
         * award_status :
         * prize_goods_name :
         * prize_goods_url :
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("express_time")
        public String expressTime;
        @SerializedName("express_id")
        public String expressId;
        @SerializedName("express_name")
        public String expressName;
        @SerializedName("express_num")
        public String expressNum;
        @SerializedName("confirm_time")
        public String confirmTime;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("award_status")
        public String awardStatus;
        @SerializedName("prize_goods_name")
        public String prizeGoodsName;
        @SerializedName("prize_goods_url")
        public String prizeGoodsUrl;
        @SerializedName("award_user_nick")
        public String awardUserNick;
        @SerializedName("award_phone")
        public String awardPhone;
        @SerializedName("award_address")
        public String awardAddress;
        @SerializedName("send_remark")
        public String sendRemark;
    }
}

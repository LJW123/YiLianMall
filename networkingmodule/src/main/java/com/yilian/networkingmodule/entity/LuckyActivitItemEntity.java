package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @atuhor Created by  on 2017/10/30 0030.
 * 首页活动列表
 */

public class LuckyActivitItemEntity extends HttpResultBean {

    /**
     * code :
     * list : [{"activity_id":"","snatch_issue":"","snatch_name":"","snatch_subhead":"","snatch_filiale":"","snatch_zone":"","snatch_goods_url":"","snatch_total_count":"","snatch_play_count":"","snatch_once_expend":"","snatch_announce_time":""},{}]
     * count :
     * server_time :
     */

    @SerializedName("code")
    public String codeX;
    @SerializedName("count")
    public String count;
    @SerializedName("server_time")
    public String serverTime;
    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * activity_id :
         * snatch_issue :
         * snatch_name :
         * snatch_subhead :
         * snatch_filiale :
         * snatch_zone :
         * snatch_goods_url :
         * snatch_total_count :
         * snatch_play_count :
         * snatch_once_expend :
         * snatch_announce_time :
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_subhead")
        public String snatchSubhead;
        @SerializedName("snatch_filiale")
        public String snatchFiliale;
        @SerializedName("snatch_zone")
        public String snatchZone;
        @SerializedName("snatch_goods_url")
        public String snatchGoodsUrl;
        @SerializedName("snatch_total_count")
        public String snatchTotalCount;
        @SerializedName("snatch_play_count")
        public String snatchPlayCount;
        @SerializedName("snatch_once_expend")
        public String snatchOnceExpend;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
    }
}

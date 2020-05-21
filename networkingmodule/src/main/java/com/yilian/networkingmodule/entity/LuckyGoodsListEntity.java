package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/11/2 0002.
 */

public class LuckyGoodsListEntity extends HttpResultBean {

    /**
     * code :
     * list : [{"activity_id":"","snatch_issue":"","snatch_name":"","snatch_subhead":"","snatch_goods_url":"","snatch_total_count":"","snatch_play_count":"","snatch_once_expend":"","snatch_announce_time":""},{}]
     * server_time :
     */
    @SerializedName("merchant_name")
    public String merchantName;
    @SerializedName("server_time")
    public String serverTime;
    @SerializedName("list")
    public List<ListBean> list;
    @SerializedName("count")
    public String count;

    public static class ListBean {
        /**
         * activity_id :
         * snatch_issue :
         * snatch_name :
         * snatch_subhead :
         * snatch_goods_url :
         * snatch_total_count :
         * snatch_play_count :
         * snatch_once_expend :
         * snatch_announce_time :
         * extra_bean :
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_subhead")
        public String snatchSubhead;
        @SerializedName("snatch_goods_url")
        public String snatchGoodsUrl;
        @SerializedName("snatch_total_count")
        public int snatchTotalCount;
        @SerializedName("snatch_play_count")
        public int snatchPlayCount;
        @SerializedName("snatch_once_expend")
        public String snatchOnceExpend;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        /**
         * 赠送益豆
         */
        @SerializedName("extra_bean")
        public String extraBean;

    }
}

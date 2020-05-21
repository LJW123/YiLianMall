package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author XiuRenLi  PRAY NO BUG
 * Created by Administrator on 2016/8/9.
 * 该类和OneBuyGoodsListEntity一样，所以并未使用
 */
public class OneBuyCategoryEntity extends BaseEntity {

    /**
     * list : [{"activity_id":"","snatch_issue":"","snatch_name":"","snatch_subhead":"","snatch_filiale":"","snatch_zone":"","snatch_goods_url":"","snatch_total_count":"","snatch_play_count":"","snatch_once_expend":"","snatch_announce_time":""},{}]
     * count :
     * server_time :
     */

    @SerializedName("count")
    public String count;
    @SerializedName("server_time")
    public String serverTime;
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

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
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

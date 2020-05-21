package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lefenandroid on 16/5/24.
 */
public class OneBuyGoodsListEntity extends BaseEntity{


    /**
     * list : [{"snatch_index":"","snatch_issue":"","snatch_name":"","snatch_subhead":"","snatch_zone":"","snatch_goods_url":"","snatch_total_count":"","snatch_play_count":"","snatch_once_expend":"","snatch_announce_time":""},{}]
     * count :
     */

    @SerializedName("count") // 活动商品总数
    public String count;
    /**
     * snatch_index :
     * snatch_issue :
     * snatch_name :
     * snatch_subhead :
     * snatch_zone :
     * snatch_goods_url :
     * snatch_total_count :
     * snatch_play_count :
     * snatch_once_expend :
     * snatch_announce_time :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    /**
     * server_time : 服务器时间
     */
    @SerializedName("server_time")
    public String serverTime;

    public class ListBean {
        @SerializedName("activity_id") // 活动id
        public String activityId;
        @SerializedName("snatch_issue") // 活动期号
        public String snatchIssue;
        @SerializedName("snatch_name") // 活动名称
        public String snatchName;
        @SerializedName("snatch_subhead") // 活动副标题
        public String snatchSubhead;
        @SerializedName("snatch_filiale") // 活动发布地区  1 代表总部  其他表示兑换中心
        public String snatchFiliale;
        @SerializedName("snatch_zone") // 活动分区
        public String snatchZone;
        @SerializedName("snatch_goods_url") // 商品图片地址
        public String snatchGoodsUrl;
        @SerializedName("snatch_total_count") // 共需人次
        public String snatchTotalCount;
        @SerializedName("snatch_play_count") // 以参与人次
        public String snatchPlayCount;
        @SerializedName("snatch_once_expend") // 每次活动消耗金额
        public String snatchOnceExpend;
        @SerializedName("snatch_announce_time") // 活动揭晓时间
        public String snatchAnnounceTime;

    }
}

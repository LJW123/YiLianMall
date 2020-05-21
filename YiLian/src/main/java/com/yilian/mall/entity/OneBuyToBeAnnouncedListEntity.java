package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/28.
 */
public class OneBuyToBeAnnouncedListEntity extends BaseEntity {


    /**
     * activity_id : 2
     * snatch_issue : 2
     * activity_name : 海尔洗衣机
     * snatch_subhead : 海尔冰箱十元购
     * snatch_start_time : 20160523
     * snatch_announce_time : 20160527
     * snatch_goods_id : 3
     * snatch_zone : 1
     * snatch_goods_album : ["uploads/goods/goods_info/201509/20150910140714_62363jpg"]
     * snatch_total_count : 500
     * snatch_play_count : 400
     */

    @SerializedName("snatch_info")
    public SnatchInfoBean snatchInfo;
    /**
     * snatch_info : {"activity_id":"2","snatch_issue":"2","activity_name":"海尔洗衣机","snatch_subhead":"海尔冰箱十元购","snatch_start_time":"20160523","snatch_announce_time":"20160527","snatch_goods_id":"3","snatch_zone":"1","snatch_goods_album":["uploads/goods/goods_info/201509/20150910140714_62363jpg"],"snatch_total_count":"500","snatch_play_count":"400"}
     * snatch_log : []
     * user_play_count : -1
     * win_log : {"win_num":"","time":"","phone":"","user_name":"","user_id":""}
     * snatch_server_time : 1464417671
     */

    @SerializedName("user_play_count")
    public int userPlayCount;
    /**
     * win_num :
     * time :
     * phone :
     * user_name :
     * user_id :
     */

    @SerializedName("win_log")
    public WinLogBean winLog;
    @SerializedName("snatch_server_time")
    public int snatchServerTime;
    @SerializedName("snatch_log")
    public List<?> snatchLog;

    public static class SnatchInfoBean {
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("activity_name")
        public String activityName;
        @SerializedName("snatch_subhead")
        public String snatchSubhead;
        @SerializedName("snatch_start_time")
        public String snatchStartTime;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("snatch_goods_id")
        public String snatchGoodsId;
        @SerializedName("snatch_zone")
        public String snatchZone;
        @SerializedName("snatch_total_count")
        public String snatchTotalCount;
        @SerializedName("snatch_play_count")
        public String snatchPlayCount;
        @SerializedName("snatch_goods_album")
        public List<String> snatchGoodsAlbum;
    }

    public static class WinLogBean {
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("time")
        public String time;
        @SerializedName("phone")
        public String phone;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_id")
        public String userId;
    }
}

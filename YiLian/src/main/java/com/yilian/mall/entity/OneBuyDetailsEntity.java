package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lefenandroid on 16/5/27.
 */
public class OneBuyDetailsEntity extends BaseEntity {

    /**
     * code :
     * server_time : 服务器时间
     * snatch_info : {"activity_id":"活动id","snatch_issue":"活动期号","activity_name":"活动名称","snatch_subhead":"活动副标题","snatch_start_time":"活动开始时间","snatch_announce_time":"揭晓时间","snatch_server_time":"服务器时间","snatch_goods_id":"商品id","snatch_goods_album":["商品相册地址",""],"snatch_total_count":"共需人次","snatch_play_count":"已参与人次","snatch_log":[{"user_name":"用户昵称或者","ip":"用户昵称或者","count":"本次参与次数","time":"本次参与时间"},{}],"win_log":{"user_name":"用户昵称或者","user_id":"用户id","phone":"用户昵称或者","count":"本次参与次数","time":"本次参与时间","win_num":"中奖号码"}}
     * user_play_count : 用户已参与人次 -1 表示用户未登录
     */
    @SerializedName("server_time")
    public String serverTime;
    @SerializedName("snatch_info")
    public SnatchInfoBean snatchInfo;
    @SerializedName("user_play_count")
    public int userPlayCount;
    @SerializedName("win_log")
    public WinLogBean winLog;
    @SerializedName("snatch_log")
    public ArrayList<SnatchLogBean> snatchLog;

    /**
     * activity_id : 活动id
     * snatch_issue : 活动期号
     * activity_name : 活动名称
     * snatch_subhead : 活动副标题
     * snatch_zone : 分区类型
     * snatch_start_time : 活动开始时间
     * snatch_announce_time : 揭晓时间
     * snatch_server_time : 服务器时间
     * snatch_goods_id : 商品id
     * snatch_goods_album : ["商品相册地址",""]
     * snatch_total_count : 共需人次
     * snatch_play_count : 已参与人次
     * snatch_log : [{"user_name":"用户昵称或者","ip":"用户昵称或者","count":"本次参与次数","time":"本次参与时间"},{}]
     * win_log : {"user_name":"用户昵称或者","user_id":"用户id","phone":"用户昵称或者","count":"本次参与次数","time":"本次参与时间","win_num":"中奖号码"}
     */
    public class SnatchInfoBean {
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("activity_name")
        public String activityName;
        @SerializedName("snatch_subhead")
        public String snatchSubhead;
        @SerializedName("snatch_once_expend")
        public double snatchOnceExpend;
        @SerializedName("snatch_zone")
        public int snatchZone;
        @SerializedName("snatch_start_time")
        public String snatchStartTime;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("snatch_server_time")
        public String snatchServerTime;
        @SerializedName("snatch_goods_id")
        public String snatchGoodsId;
        @SerializedName("snatch_total_count")
        public String snatchTotalCount;
        @SerializedName("snatch_play_count")
        public String snatchPlayCount;

        @SerializedName("snatch_goods_album")
        public ArrayList<String> snatchGoodsAlbum;



    }

    /**
     * user_name : 用户昵称或者
     * user_id : 用户id
     * phone : 用户昵称或者
     * count : 本次参与次数
     * time : 本次参与时间
     * win_num : 中奖号码
     */
    public class WinLogBean {
        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
        @SerializedName("count")
        public String count;
        @SerializedName("time")
        public String time;
        @SerializedName("win_num")
        public String winNum;
    }

    /**
     * user_name : 用户昵称或者
     * ip : 用户昵称或者
     * count : 本次参与次数
     * time : 本次参与时间
     */
    public class SnatchLogBean {
        @SerializedName("photo")
        public String photo;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("ip")
        public String ip;
        @SerializedName("count")
        public String count;
        @SerializedName("time")
        public String time;
        @SerializedName("user_id")
        public String user_id;

        @SerializedName("ip_address")
        public String ip_address;
    }
}



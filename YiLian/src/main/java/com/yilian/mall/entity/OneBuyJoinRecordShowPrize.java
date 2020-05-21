package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/3.
 * 登录用户本人晒单列表
 */
public class OneBuyJoinRecordShowPrize extends BaseEntity {

    /**
     * comment_index : 37
     * snatch_id : 1
     * time : 1464486484
     * user_id : 2740558912591517
     * comment_content : dsfs
     * comment_images : uploads/goods/goods_info/201509/20150910140714_62363jpg
     * snatch_name : 海尔冰箱
     * snatch_issue : 2
     * snatch_announce_time : 1464671098
     * photo : false
     * win_num : 1
     * user_name : 刘翔
     * snatch_log : [{"ip":"","count":"9","time":"1481558399"}]
     */

    @SerializedName("snatch_info")
    public ArrayList<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean {
        @SerializedName("comment_index")
        public String commentIndex;
        @SerializedName("snatch_id")
        public String snatchId;
        @SerializedName("time")
        public String time;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("comment_content")
        public String commentContent;
        @SerializedName("comment_images")
        public String commentImages;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("photo")
        public boolean photo;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("user_name")
        public String userName;
        /**
         * ip :
         * count : 9
         * time : 1481558399
         */

        @SerializedName("snatch_log")
        public ArrayList<SnatchLogBean> snatchLog;

        public static class SnatchLogBean {
            @SerializedName("ip")
            public String ip;
            @SerializedName("count")
            public String count;
            @SerializedName("time")
            public String time;
        }
    }
}

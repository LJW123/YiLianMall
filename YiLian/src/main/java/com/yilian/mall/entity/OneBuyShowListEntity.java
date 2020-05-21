package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lefenandroid on 16/5/24.
 */
public class OneBuyShowListEntity extends BaseEntity {


    /**
     * user_name :
     * time :
     * snatch_name :
     * snatch_issue :
     * comment_images :
     * comment_content :
     * snatch_announce_time :
     * snatch_log : [{"user_name":"","ip":"","count":"","time":"","win_num":""},{}]
     */

    @SerializedName("snatch_info")
    public List<SnatchInfoBean> snatchInfo;

    public class SnatchInfoBean {
        @SerializedName("user_name")
        public String userName;
        @SerializedName("time")
        public String time;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("comment_images")
        public String commentImages;
        @SerializedName("comment_content")
        public String commentContent;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        /**
         * user_name :
         * ip :
         * count :
         * time :
         * win_num :
         */

        @SerializedName("snatch_log")
        public List<SnatchLogBean> snatchLog;

        public class SnatchLogBean {
            @SerializedName("user_name")
            public String userName;
            @SerializedName("ip")
            public String ip;
            @SerializedName("count")
            public String count;
            @SerializedName("time")
            public String time;
            @SerializedName("win_num")
            public String winNum;
        }
    }
}

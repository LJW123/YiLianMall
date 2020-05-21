package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by LYQ on 2017/10/28.
 */

public class SnatchShowListEntity extends HttpResultBean {

    /**
     * code :
     * snatch_info : [{"user_name":"","user_id":"","photo":"","time":"","snatch_name":"","snatch_issue":"","comment_index":"","comment_images":"","comment_content":"","snatch_announce_time":"","win_num":"","snatch_log":[{"ip":"","count":"","time":""},{}]},{}]
     */

    @SerializedName("snatch_info")
    public List<SnatchInfoBean> snatchInfo;

    public class SnatchInfoBean {
        /**
         * user_name :
         * user_id :
         * photo :
         * time :
         * snatch_name :
         * snatch_issue :
         * comment_index :
         * comment_images :
         * comment_content :
         * snatch_announce_time :
         * win_num :
         * snatch_log : [{"ip":"","count":"","time":""},{}]
         */

        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("photo")
        public String photo;
        @SerializedName("time")
        public String time;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("comment_index")
        public String commentIndex;
        @SerializedName("comment_images")
        public String commentImages;
        @SerializedName("comment_content")
        public String commentContent;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("snatch_log")
        public List<SnatchLogBean> snatchLog;

        public  class SnatchLogBean {
            /**
             * ip :
             * count :
             * time :
             */

            @SerializedName("ip")
            public String ip;
            @SerializedName("count")
            public String count;
            @SerializedName("time")
            public String time;
        }
    }
}

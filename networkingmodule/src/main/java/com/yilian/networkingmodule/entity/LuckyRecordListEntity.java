package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/10/30 0030
 */

public class LuckyRecordListEntity extends HttpResultBean {

    @SerializedName("snatch_info")
    public ArrayList<SnatchInfo> snatch_info;

    public class SnatchInfo {
        @SerializedName("activity_id")
        public String activity_id;
        @SerializedName("snatch_issue")
        public String snatch_issue;
        @SerializedName("snatch_announce_time")
        public String snatch_announce_time;
        @SerializedName("snatch_name")
        public String snatch_name;
        @SerializedName("win_num")
        public String win_num;
        @SerializedName("join_count")
        public String join_count;
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("photo")
        public String photo;
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/31.
 */
public class OneBuyJoinRecordAnnouncedEntity extends BaseEntity {


    /**
     * activity_id : 3
     * snatch_status : 2
     * snatch_issue : 4
     * snatch_name : 海尔冰箱
     * goods_url : uploads/goods/goods_info/201509/20150910140714_62363jpg
     * snatch_announce_time : 1464671098
     * join_count : 400
     * total_count : 500
     * win_num : 3
     * user_name : 哭
     * phone : 13526787582
     * mycount : 13
     * awardcount : 0
     * award_staus : -1
     * type : 2
     */

    @SerializedName("snatch_info")
    public ArrayList<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean {
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_status")
        public String snatchStatus;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("goods_url")
        public String goodsUrl;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("total_count")
        public String totalCount;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("mycount")
        public String mycount;
        @SerializedName("awardcount")
        public int awardcount;
        @SerializedName("award_staus")
        public int awardStaus;
        @SerializedName("type")
        public int type;
    }
}

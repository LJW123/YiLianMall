package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/31.
 */
public class OneBuyJoinRecordGoingEntity extends BaseEntity {
    /**
     * activity_id : 41
     * snatch_status : 1
     * snatch_issue : 456791
     * snatch_name : XXXX
     * goods_url : uploads/goods/goods_info/201509/20150910140714_62363jpg
     * snatch_announce_time : 0
     * join_count : 2
     * total_count : 24
     * award_status : 0
     * type : 1
     * mycount : 2
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
        @SerializedName("award_status")
        public int awardStatus;
        @SerializedName("type")
        public int type;
        @SerializedName("mycount")
        public String mycount;

        @Override
        public String toString() {
            return "SnatchInfoBean{" +
                    "activityId='" + activityId + '\'' +
                    ", snatchStatus='" + snatchStatus + '\'' +
                    ", snatchIssue='" + snatchIssue + '\'' +
                    ", snatchName='" + snatchName + '\'' +
                    ", goodsUrl='" + goodsUrl + '\'' +
                    ", snatchAnnounceTime='" + snatchAnnounceTime + '\'' +
                    ", joinCount='" + joinCount + '\'' +
                    ", totalCount='" + totalCount + '\'' +
                    ", awardStatus=" + awardStatus +
                    ", type=" + type +
                    ", mycount='" + mycount + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OneBuyJoinRecordGoingEntity{" +
                "snatchInfo=" + snatchInfo.toString() +
                '}';
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/6.
 */
public class OneBuyRecommendEntity extends BaseEntity {

    /**
     * activity_id : 16
     * snatch_issue : 456789
     * snatch_name : XXXX
     * snatch_goods_url :
     * join_count : 5
     * snatch_total_count : 5
     */

    @SerializedName("snatch_info")
    public ArrayList<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean {
        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("snatch_goods_url")
        public String snatchGoodsUrl;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("snatch_total_count")
        public String snatchTotalCount;
    }
}

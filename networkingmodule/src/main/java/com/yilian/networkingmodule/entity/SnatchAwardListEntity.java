package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by LYQ on 2017/10/28.
 */

public class SnatchAwardListEntity extends HttpResultBean {

    /**
     * code :
     * snatch_info : [{"activity_id":"","snatch_issue":"","snatch_name":"","goods_url":"","snatch_announce_time":"","win_num":"","join_count":"","total_count":"","mycount":"","award_status":"","user_name":"","phone":"","photo":"","award_count":"","award_num":"","award_time":""},{}]
     */

    @SerializedName("snatch_info")
    public List<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean implements MultiItemEntity {
        /**
         * activity_id :
         * snatch_issue :
         * snatch_name :
         * goods_url :
         * snatch_announce_time :
         * win_num :
         * join_count :
         * total_count :
         * mycount :
         * award_status :
         * user_name :
         * phone :
         * photo :
         * award_count :
         * award_num :
         * award_time :
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("snatch_issue")
        public String snatchIssue;
        @SerializedName("snatch_name")
        public String snatchName;
        @SerializedName("goods_url")
        public String goodsUrl;
        @SerializedName("snatch_announce_time")
        public String snatchAnnounceTime;
        @SerializedName("win_num")
        public String winNum;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("total_count")
        public String totalCount;
        @SerializedName("mycount")
        public String mycount;
        @SerializedName("award_status")
        public String awardStatus;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
        @SerializedName("awardcount")
        public String awardCount;
        @SerializedName("award_num")
        public String awardNum;
        @SerializedName("award_time")
        public String awardTime;
        @SerializedName("type")
        public int type;
        /**
         * 是否下架
         * 0 不是紧急下架  信息展示参照之前  1 紧急下架  那些中奖人信息不存在
         */
        @SerializedName("undercarriage")
        public int isDownLine;

        @Override
        public int getItemType() {
            if (isDownLine == 1) {
                type = 3;
            }
            return type;
        }
    }
}

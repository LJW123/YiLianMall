package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 * 登录用户本人中奖记录
 */
public class OneBuyJoinRecordWinPrizeEntity extends BaseEntity {

    /**
     * snatch_name : 海尔洗衣机
     * goods_url : uploads/goods/goods_info/201509/20150910140714_62363jpg
     * snatch_issue : 0
     * snatch_announce_time : 1464340409
     * user_name : 刘翔
     * phone : false
     * user_id : 2740558912591517
     * win_num : 1
     * status : 2
     * join_count : 9
     */

    @SerializedName("snatch_info")
    public List<SnatchInfoBean> snatchInfo;

    public static class SnatchInfoBean {
        @SerializedName("snatch_name")//活动标题
        public String snatchName;
        @SerializedName("goods_url")//图片
        public String goodsUrl;
        @SerializedName("snatch_issue")//活动期号
        public String snatchIssue;
        @SerializedName("snatch_announce_time")//揭晓时间
        public String snatchAnnounceTime;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("phone")
        public boolean phone;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("win_num")//中奖号码
        public String winNum;
        @SerializedName("status")//奖励状态：0表示未开奖，1表示未设置收货地址，2待发货，3，已发货
        public String status;
        @SerializedName("join_count")//活动参与次数
        public String joinCount;
    }
}

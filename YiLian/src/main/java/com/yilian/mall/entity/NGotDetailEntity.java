package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/27.
 */
public class NGotDetailEntity extends BaseEntity {

    @SerializedName("snatch_info")
    public NGotDetail snatch_info;

    public class NGotDetail{
        @SerializedName("prize_goods_name")
        public String prize_goods_name;
        @SerializedName("express_num")//快递名称
        public String express_num;
        @SerializedName("set_address_time")//设置地址时间
        public String set_address_time;
        @SerializedName("activity_id")
        public String activity_id;
        @SerializedName("express_time")//发货时间
        public String express_time;
        @SerializedName("express_id")//快递id
        public String express_id;
        @SerializedName("prize_goods_url")
        public String prize_goods_url;
        @SerializedName("join_count")//活动参与次数
        public String join_count;
        @SerializedName("snatch_announce_time")//揭晓时间
        public String snatch_announce_time;
        @SerializedName("award_status")//奖励状态：0表示未开奖，1表示未设置收货地址，2待发货，3，已发货 ,4已完成
        public String award_status;
        @SerializedName("win_num")//中奖号码
        public String win_num;
        @SerializedName("snatch_issue")
        public String snatch_issue;
        @SerializedName("express_name")//快递公司
        public String express_name;
        @SerializedName("confirm_time")//收货时间
        public String confirm_time;
    }


}

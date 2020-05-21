package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/5.
 */
public class SnatchListEntity extends BaseEntity {

    /**
     * 夺宝列表
     */
    @SerializedName("list")
    public ArrayList<Goods> goodsList;

    public class Goods {
        @SerializedName("activity_name")
        public String activityName;
        @SerializedName("activity_status")
        public String activityStatus;
        @SerializedName("activity_submit_time")//活动截止时间
        public String activity_submit_time;
        @SerializedName("activity_id")//活动id
        public String activityId;
        @SerializedName("goods_icon")//商品图
        public String goodsIcon;
        @SerializedName("activity_time")//活动时间
        public String activityTime;
        @SerializedName("goods_name")//商品名称
        public String goodsName;
        @SerializedName("total_count")//共需人次
        public int totalCount;
        @SerializedName("join_count")//已参与人次
        public int joinCount;
        @SerializedName("has_end")///是否已结束  1正在进行中 2结束
        public String hasEnd;
        @SerializedName("has_prize")///是否中奖  是否中奖 0未中奖 1中奖
        public int hasPrize;
        @SerializedName("has_address")///是否设置收货地址  //是否设置收货地址 0未设置 1已经设置过
        public int hasAddress;

    }
}

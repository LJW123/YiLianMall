package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/6.
 * 夺宝详情进行中
 */
public class SnatchPartEntity extends BaseEntity {


    @SerializedName("status")//进行状态  1进行中  2已揭晓
    public String status;
    @SerializedName("activity")//单个商品详细数据
    public Act activity;

    @SerializedName("log")
    public ArrayList<Record> log0;

    public class Act {
        @SerializedName("activity_id")//活动id
        public String activityId;
        @SerializedName("activity_name")//活动名
        public String activityName;
        @SerializedName("activity_time")
        public String activityTime;
        @SerializedName("activity_expend")
        public String activity_expend;
        @SerializedName("goods_name")//商品名字
        public String goodsName;
        @SerializedName("goods_album")//商品相册地址
        public ArrayList<String> goodsAlbum;
        @SerializedName("goods_introduce")//商品介绍图集
        public ArrayList goodsIntroduce;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("total_count")
        public int totalCount;
        @SerializedName("join_count")
        public int joinCount;

    }

    public class Record {
        @SerializedName("join_time")//参与时间
        public String joinTime;
        @SerializedName("photo")//头像
        public String photo;
        @SerializedName("phone")//用户手机号
        public String phone;

    }

}

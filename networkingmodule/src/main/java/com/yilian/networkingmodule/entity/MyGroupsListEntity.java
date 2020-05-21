package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/19 0019.
 */
public class MyGroupsListEntity extends BaseEntity {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * activity_id : 2
         * done_group_time : 1495444142
         * end_group_time : 1495444142
         * end_time : 1508990400
         * goods_count : 1
         * goods_icon : goods/147832446175605.jpg
         * goods_name : adidasi-韩国进口棉袜_韩国进口女士棉袜beimon品牌短袜时尚全棉防臭休闲
         * goods_sku_name : 尺码：有码，颜色：无色
         * group_condition : 8
         * group_id : 1
         * group_price : 1
         * join_amount : 1
         * label : 抽奖团
         * lead_user_id : 5702006601841715
         * open_group_time : 1495444142
         * open_prize_time : 0
         * participants : 0
         * status : 1
         * time : 1495539065
         * order_id : 2017052315595944899
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("done_group_time")
        public String doneGroupTime;
        @SerializedName("end_group_time")
        public String endGroupTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("goods_count")
        public int goodsCount;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_sku_name")
        public String goodsSkuName;
        @SerializedName("group_condition")
        public String groupCondition;
        @SerializedName("group_id")
        public String groupId;
        @SerializedName("group_price")
        public String groupPrice;
        @SerializedName("join_amount")
        public String joinAmount;
        @SerializedName("label")
        public String label;
        @SerializedName("lead_user_id")
        public String leadUserId;
        @SerializedName("open_group_time")
        public String openGroupTime;
        @SerializedName("open_prize_time")
        public String openPrizeTime;
        @SerializedName("participants")
        public String participants;
        @SerializedName("status")
        public String status;
        @SerializedName("time")
        public String time;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_index")
        public String orderIndex;
    }
}

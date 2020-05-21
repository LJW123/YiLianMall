package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2017/4/27 0027.
 * 大转盘获奖名单列表
 */

public class WheelOfFortuneAwardListEntity extends BaseEntity implements Serializable {

    /**
     * data : {"list":[{"order_id":"","name":"","winning_time":"","done_time":"","unuse_time":""},{}]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable {
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public class ListBean implements Serializable {
            /**
             * order_id :
             * name :
             * winning_time :
             * done_time :
             * unuse_time :
             */
            @SerializedName("phone")
            public String winnerName;
            @SerializedName("order_id")
            public String orderId;
            @SerializedName("name")
            public String goodName;
            @SerializedName("winning_time")
            public String winningTime;
            @SerializedName("done_time")
            public String doneTime;
            @SerializedName("unuse_time")
            public String unuseTime;
            @SerializedName("type")
            public int type;
            @SerializedName("goods_type")
            public String goods_type;
            @SerializedName("goods_id")
            public String goods_id;
            @SerializedName("goods_price")
            public String goods_price;
            @SerializedName("goods_cost")
            public String goods_cost;
            @SerializedName("sku_index")
            public String sku_index;
            @SerializedName("goods_icon")
            public String goods_icon;
            @SerializedName("supplier_name")
            public String supplier_name;
            @SerializedName("round")
            public String round;
            @SerializedName("order_index")
            public String order_Index;
            @SerializedName("goods_norms")
            public ArrayList<String> normsList;

            public String skuStr;
        }
    }
}

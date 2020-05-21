package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2016/10/24 0024.
 * 我的收藏商品示例
 */

public class JPMyFavoriteGoodsEntity extends BaseEntity {


    /**
     * collect_index :
     * collect_id :
     * collect_name :
     * collect_icon :
     * collect_type :
     * collect_time :
     * filiale_id :
     * collect_price :
     * collect_cost :
     */


    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("collect_index")
        public String collectIndex;
        @SerializedName("collect_id")
        public String collectId;
        @SerializedName("collect_name")
        public String collectName;
        @SerializedName("collect_icon")
        public String collectIcon;
        @SerializedName("collect_type")
        public String collectType;
        @SerializedName("collect_time")
        public String collectTime;
        @SerializedName("filiale_id")
        public String filialeId;
        @SerializedName("collect_price")
        public String collectPrice;
        @SerializedName("collect_cost")
        public String collectCost;
        @SerializedName("status")
        public String status;
        @SerializedName("return_integral")
        public String  returnIntegral;//返利的奖券
        @SerializedName("type")
        public String type;//0 普通商品 1易划算 2 奖券购
        @SerializedName("goods_integral")
        public String goodsIntegral;//易划算价格

    }
}

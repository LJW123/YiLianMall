package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2016/11/9 0009.
 */

public class JPMallGoodsListEntity extends BaseEntity {

    /**
     * goods_name :
     * filiale_id :
     * goods_supplier :
     * goods_id :
     * goods_price :
     * goods_cost :
     * goods_icon :
     * goods_grade :
     * has_goods :
     * tags_name :
     * tags_id :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("filiale_id")
        public String filialeId;
        @SerializedName("goods_supplier")
        public String goodsSupplier;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_grade")
        public String goodsGrade;
        @SerializedName("has_goods")
        public String hasGoods;
        @SerializedName("tags_name")
        public String tagsName;
        @SerializedName("tags_id")
        public String tagsId;
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("sell_count")
        public String sellCount;

        @Override
        public String toString() {
            return "ListBean{" +
                    "goodsName='" + goodsName + '\'' +
                    ", filialeId='" + filialeId + '\'' +
                    ", goodsSupplier='" + goodsSupplier + '\'' +
                    ", goods_id='" + goodsId + '\'' +
                    ", goodsPrice='" + goodsPrice + '\'' +
                    ", goodsCost='" + goodsCost + '\'' +
                    ", goodsIcon='" + goodsIcon + '\'' +
                    ", goodsGrade='" + goodsGrade + '\'' +
                    ", hasGoods='" + hasGoods + '\'' +
                    ", tagsName='" + tagsName + '\'' +
                    ", tagsId='" + tagsId + '\'' +
                    '}';
        }
    }
}

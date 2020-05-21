package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2016/09/06.
 */
public class ShoppingCartListEntity2 extends BaseEntity {


    /**
     * goods_id : 877
     * goods_norms :
     * goods_count : 1
     * filiale_id : 0
     * supplier_id : 0
     * region_id : 0
     * device_index : 0
     * cart_id : 824
     * goods_name : 潘婷强韧防掉发洗发露200ml
     * goods_price : 1950 如果是乐享区商品 该值就是市场价，如果是乐分区商品，该值就是乐分价
     * sku_inventory : 895
     * goods_discount : 0
     * goods_type : 3是乐分区商品  4乐享区商品  5零购券区商品
     * goods_icon : uploads/goods/goods_info/20160220/20160220170718_495093.jpg
     * create_time : 1462432530
     * goods_status : 0
     * shop_id : 0
     * shop_type : 0
     * shop_name : 乐分总部
     * goods_cost 如果是乐享区商品 该值就是优惠后的价格 如果是乐分区 该值为0
     * price-cost 是乐享区商品抵扣抵扣券数量
     */

    @SerializedName("list")
    public ArrayList<ArrayList<ListBean>> list;

    public static class ListBean implements Serializable {
        @SerializedName("return_integral")
        public float returnIntegral;
        @SerializedName("return_bean")
        public float returnBean;

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_norms")
        public String goodsNorms;
        @SerializedName("goods_count")
        public int goodsCount;
        @SerializedName("filiale_id")
        public String filialeId;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("region_id")
        public String regionId;
        @SerializedName("device_index")
        public String deviceIndex;
        @SerializedName("cart_id")
        public String cartId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_price")
        public float goodsPrice;
        @SerializedName("goods_cost")
        public float goodsCost;
        @SerializedName("sku_inventory")
        public String skuInventory;
        @SerializedName("goods_discount")
        public int goodsDiscount;
        //type
        @SerializedName("goods_type")
        public int goodsType;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("create_time")
        public String createTime;
        @SerializedName("goods_status")
        public int goodsStatus;
        @SerializedName("shop_id")
        public int shopId;
        @SerializedName("shop_type")
        public int shopType;
        @SerializedName("shop_name")
        public String shopName;
        @SerializedName("is_global")
        public String isGlobal;
        @SerializedName("type")
        public String type;//type 0普通商品 1易划算 2 奖券购
        @SerializedName("goods_integral")
        public float goodsIntegral;//只有是易划算时才返回奖券字段
        @SerializedName("goods_retail")
        public float goodsRetail;//奖券购的现金

        public boolean isChecked;

        @Override
        public String toString() {
            return "ListBean{" +
                    "returnIntegral=" + returnIntegral +
                    ", goodsId='" + goodsId + '\'' +
                    ", goodsNorms='" + goodsNorms + '\'' +
                    ", goodsCount=" + goodsCount +
                    ", filialeId='" + filialeId + '\'' +
                    ", supplierId='" + supplierId + '\'' +
                    ", regionId='" + regionId + '\'' +
                    ", deviceIndex='" + deviceIndex + '\'' +
                    ", cartId='" + cartId + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsPrice=" + goodsPrice +
                    ", goodsCost=" + goodsCost +
                    ", skuInventory='" + skuInventory + '\'' +
                    ", goodsDiscount=" + goodsDiscount +
                    ", goodsType=" + goodsType +
                    ", goodsIcon='" + goodsIcon + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", goodsStatus=" + goodsStatus +
                    ", shopId=" + shopId +
                    ", shopType=" + shopType +
                    ", shopName='" + shopName + '\'' +
                    ", isGlobal='" + isGlobal + '\'' +
                    ", type='" + type + '\'' +
                    ", goodsIntegral=" + goodsIntegral +
                    ", goodsRetail=" + goodsRetail +
                    ", isChecked=" + isChecked +
                    '}';
        }
    }
}

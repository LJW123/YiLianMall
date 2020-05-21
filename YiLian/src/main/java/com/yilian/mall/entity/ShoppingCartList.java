package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 刘玉坤 on 2016/1/22.
 */
public class ShoppingCartList extends BaseEntity {

    public ArrayList<ShoppingCartListItem> list;


    public class ShoppingCartListItem implements Serializable {

        /**
         * 购物车id
         */
        @SerializedName("cart_id")
        public String shoppingCartId;

        /**
         * 商品Id
         */
        @SerializedName("goods_id")
        public String goodsId;

        /**
         * 商品数量
         */
        @SerializedName("goods_count")
        public int goodsCount;

        /**
         * 商品名字
         */
        @SerializedName("goods_name")
        public String goodsName;

        /**
         * 商品价格
         */
        @SerializedName("goods_price")
        public int goodsPrice;

        /**
         * 商品图片路径
         */
        @SerializedName("goods_icon")
        public String goodsIcon;

        /**
         * 商品规格中文描述
         */
        @SerializedName("goods_norms")
        public String goodsNorms;

        /**
         * 添加时间
         */
        @SerializedName("create_time")
        public String createTime;

        /**
         * 商品状态 0默认 1已下架 2已售完
         */
        @SerializedName("goods_status")
        public int goodsStatus;

        /**
         * 10或20，代表服务费百分比
         */
        @SerializedName("goods_discount")
        public int goodsDiscount;

        /**
         * 3乐购区（送区）4买区
         */
        @SerializedName("goods_type")
        public int goodsType;

        public boolean isChecked;

    }
}

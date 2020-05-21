package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/22.
 */
public class NotEvaluateList extends BaseEntity {

    public ArrayList<Evaluate> list;

    public class Evaluate {
        @SerializedName("order_goods_index")//商品订单规格自增编号
        public String orderGoodsIndex;
        @SerializedName("goods_index")//b2c商城商品编号
        public String goodsIndex;
        @SerializedName("goods_name")//商品名称
        public String goodsName;
        @SerializedName("order_index")//订单编号
        public String orderIndex;
        @SerializedName("goods_norms")//商品规格相关信息"颜色 红色 尺码 37"
        public String goodsNorms;
        @SerializedName("goods_count")//商品数量
        public String goodsCount;
        @SerializedName("goods_price")//商品价格
        public String goodsPrice;
        @SerializedName("goods_type")//商品类型
        public String goodsType;
        @SerializedName("goods_icon")//商品缩略图
        public String goodsIcon;
    }

}

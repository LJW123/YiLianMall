package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by 刘玉坤 on 2016/2/26.
 */
public class AfterSale extends BaseEntity {


    public ArrayList<AfterSaleList> list;

    public class AfterSaleList {
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("service_id")
        public String serviceId;

        @SerializedName("type")//售后类型 0换货返修 1退货
        public int AfterSaleType;

        @SerializedName("barter_type") //用户选择的售后方式，1换货、2返修
        public int barterType;

        @SerializedName("order_id")//订单号
        public String orderId;

        @SerializedName("service_order")
        public String serviceOrder;

        @SerializedName("status")//订单状态：0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        public int AfterSaleStatus;

        @SerializedName("goods_price")//退货商品单价
        public String goodsPrice;

        @SerializedName("goods_count")//退货数量
        public String returnGoodsCount;

        @SerializedName("goods_id")//商品唯一索引
        public String goodsId;

        @SerializedName("goods_name")//商品名称
        public String goodsName;

        @SerializedName("goods_icon")//商品缩略图
        public String goodsIcon;

        @SerializedName("goods_norms")//商品sku描述
        public String goodsNorms;

        @SerializedName("service_time")//申请时间
        public String AfterSaleTime;

        @SerializedName("goods_discount")//10或20，代表服务费百分比
        public float goodsDiscount;

        @SerializedName("goods_type")//3乐购区（送区）4买区
        public int goodsType;

        @SerializedName("goods_filiale")
        public String goodsFiliale;

        @SerializedName("goods_cost")
        public String goodsCost;

        @SerializedName("pay_style")
        public String payStyle;//0普通商品 1易划算 2奖券购

        @SerializedName("goods_integral")
        public String goodsIntegral;//易划算奖券价格

        @SerializedName("goods_retail")
        public String goodsRetail;//奖券购的商品价
        @SerializedName("given_bean")
        public String giveBean;

        /**
         * 可得益豆
         */
        @SerializedName("return_bean")
        public String returnBean;
        /**
         * 平台加赠益豆
         */
        public String subsidy;
    }

}

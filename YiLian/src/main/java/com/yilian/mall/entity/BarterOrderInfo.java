package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/2/21.
 *  返修/换货 订单信息
 */
public class BarterOrderInfo extends BaseEntity {
    public Barter barter;

    public class Barter {
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("barter_index")//退款订单号
        public String barterIndex;
        @SerializedName("barter_id")//订单号
        public String barterId;
        @SerializedName("barter_order")//售后订单编号
        public String barterOrder;
        @SerializedName("barter_num") //原订单号
        public String barterNum;
        @SerializedName("order_goods_index")//商品订单自增编号
        public String orderGoodsIndex;
        @SerializedName("barter_status")//订单状态：0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        public int barterStatus;
        @SerializedName("barter_remark")//退货备注
        public String barterRemark;
        @SerializedName("barter_images")//返修 换货照片
        public String barterImages;
        @SerializedName("barter_refuse")//拒绝原因
        public String barterRefuse;
        @SerializedName("barter_time")//申请时间
        public String barterTime;
        @SerializedName("barter_type")//申请类型 1换货 2返修
        public String barterType;
        @SerializedName("barter_goods_name")//商品名称
        public String barterGoodsName;
        @SerializedName("barter_goods_price")//退货商品单价
        public String barterGoodsPrice;
        @SerializedName("barter_goods_count")//退货数量
        public String barterGoodsCount;
        @SerializedName("barter_goods_index")//商品唯一索引
        public String barterGoodsIndex;
        @SerializedName("barter_goods_sku")//商品sku
        public String barterGoodsSku;
        @SerializedName("barter_goods_norms")//商品sku描述
        public String barterGoodsNorms;
        @SerializedName("barter_goods_discount")//10或20，代表服务费百分比
        public String barterGoodsDiscount;
        @SerializedName("barter_goods_type")//3乐购区（送区）4买区
        public int barterGoodsType;
        @SerializedName("barter_goods_icon")//商品缩略图
        public String barterGoodsIcon;
        @SerializedName("barter_total_price")//退货总价
        public int barterTotalPrice;
        @SerializedName("consumer_confirm_time")//用户确认收货时间
        public String consumerConfirmTime;
        @SerializedName("return_address")//退货时，物品寄送地址
        public ReturnAddress returnAddress;
        @SerializedName("barter_goods_cost")//商品销售价格
        public String barterGoodsCost;
        @SerializedName("check_time")//审核拒绝时间
        public String checkTime;

        @SerializedName("pay_style")
        public String payStyle; //0是普通商品 1易划算 2奖券购
        @SerializedName("goods_integral")
        public String goodsIntegral;//易划算奖券
        @SerializedName("goods_retail")
        public String goodsRetail;//奖券购商品价格

        /**
         * 可得益豆
         */
        @SerializedName("return_bean")
        public String returnBean;
        /**
         * 平台加赠益豆
         */
        public String subsidy;

        /**
         *  代购券
         */
        @SerializedName("barter_quan")
        public String barterQuan;

    }
}
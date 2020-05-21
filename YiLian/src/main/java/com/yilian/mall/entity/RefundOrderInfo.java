package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘玉坤 on 2016/3/7.
 * <p>
 * 退货 订单信息
 */
public class RefundOrderInfo extends BaseEntity {

    public Refund refund;

    public class Refund {
        @SerializedName("refund_quan")
        public String refundQuanMoney;
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("refund_index")//退货编号
        public String refundIndex;
        @SerializedName("refund_id")//订单id
        public String refundId;
        @SerializedName("order_goods_index")//商品订单自增编号
        public String orderGoodsIndex;
        @SerializedName("refund_order")//售后订单编号
        public String refundOrder;
        @SerializedName("refund_num") //原订单号
        public String refundNum;
        @SerializedName("refund_status")// 0已取消，1待审核2审核拒绝3待退货4待退款5已完成
        public int refundStatus;
        @SerializedName("extra")//由于抵扣券不足，用户使用乐享币垫付的抵扣券金额
        public String extra;
        @SerializedName("refund_remark")//退货备注
        public String refundRemark;
        @SerializedName("refund_images")//退货原因照片
        public String refundImages;
        @SerializedName("refund_refuse")//退货拒绝原因
        public String refundRefuse;
        @SerializedName("refund_time")//申请时间
        public String refundTime;
        @SerializedName("payment_type")//返款款类型
        public String paymentType;
        @SerializedName("payment_deal_id")//流水编号
        public String paymentDealId;
        @SerializedName("refund_goods_price")//退货商品单价
        public String refundGoodsPrice;
        @SerializedName("refund_goods_name")//商品名称
        public String refundGoodsName;
        @SerializedName("refund_goods_count")//申请退货商品数量
        public String refundGoodsCount;
        @SerializedName("refund_goods_index")//商品编号
        public String refundGoodsIndex;
        @SerializedName("refund_goods_icon")//商品缩略图
        public String refundGoodsIcon;
        @SerializedName("refund_goods_sku")//商品sku
        public String refundGoodsSku;
        @SerializedName("refund_goods_norms")//商品sku描述
        public String refundGoodsNorms;
        @SerializedName("refund_goods_discount")//10或20，代表服务费百分比
        public String refundGoodsDiscount;
        @SerializedName("refund_goods_type")//3乐购区（送区）4买区
        public int refundGoodsType;
        @SerializedName("refund_total_price")//退款P总价
        public String refundTotalPrice;

        @SerializedName("check_time")
        public String checkTime;//审核拒绝时间
        @SerializedName("return_address")
        public ReturnAddress returnAddress;
        @SerializedName("refund_goods_cost")
        public String refundGoodsCost;

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
        @SerializedName("subsidy")
        public String subsidy;
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/26.
 *
 * @author
 */
public class MallOrderListEntity extends BaseEntity {
    /**
     * order_index :
     * order_id :
     * order_name :
     * filiale_id :
     * region_id :
     * supplier_id :
     * order_status :
     * order_time :
     * order_coupon :
     * order_goods_price :
     * order_freight_price :
     * order_total_price :
     * goods_list : [{"goods_status":"","goods_id":"","goods_name":"","goods_icon":"","goods_count":"","goods_sku":"","goods_norms":"","goods_price":"","goods_discount":"","goods_type":"","goods_after_sale":""},{}]
     */

    public ArrayList<MallOrderLists> list;

    public class MallOrderLists {
        @SerializedName("order_belongs")
        public int orderBelongs; //6表示购物卡订单  by Zg 2018/11/22
        @SerializedName("profit")
        public int profitStatus;//发奖励状态 0未发奖励  1已送奖券
        public String order_index;
        public String order_id;
        public String order_name;//代表每个订单的商家名称
        public String filiale_id;
        public String region_id;
        public String supplier_id;
        public int order_status;
        public String order_time;
        public String order_coupon;
        public String order_goods_price;//商品总价
        public String order_freight_price;//运费价格
        public String order_total_price;//订单总价
        /**
         * //2017年6月24日17:14:17 和服务器英楠沟通得知
         * 该字段重新定义 0益联订单 1乐分订单
         * 2017年10月15日又加了一个类型 2大转盘中奖订单 3刮刮乐 4大家猜 5猜价格 6碰运气
         */
        public String order_type;
        @SerializedName("order_turns")
        public int order_turns;//0普通订单 1中奖订单
        public String order_integral_price;//奖券价格

        /**
         * 猜价格-碰运气 去支付时的押金
         */
        @SerializedName("comment_deposit")
        public String comment_deposit;

        /**
         * goods_status :
         * goods_id :
         * goods_name :
         * goods_icon :
         * goods_count :
         * goods_sku :
         * goods_norms :
         * goods_price :
         * goods_discount :
         * goods_type :
         * goods_after_sale :
         */

        public ArrayList<MallOrderGoodsList> goods_list;

        public class MallOrderGoodsList {
            public int goods_status;
            public String goods_id;
            public String goods_name;
            public String goods_icon;
            public int goods_count;
            public String goods_sku;
            public String goods_norms;
            public String goods_price;
            public String goods_cost;
            public String goods_discount;
            public String goods_type;
            public String goods_after_sale;
            public String order_goods_index;
            @SerializedName("return_integral")
            public String returnIntegral;
            public String goods_retail;//奖券购的市场价
            public String goods_integral;//易划算的奖券价格
            public String type;//0正常商品 1易划算 2奖券购
            @SerializedName("given_bean")
            public String giveBean;

            /**
             * 平台加赠益豆
             */
            public String subsidy;

            @Override
            public String toString() {
                return "MallOrderGoodsList{" +
                        "goods_status=" + goods_status +
                        ", goods_id='" + goods_id + '\'' +
                        ", goods_name='" + goods_name + '\'' +
                        ", goods_icon='" + goods_icon + '\'' +
                        ", goods_count=" + goods_count +
                        ", goods_sku='" + goods_sku + '\'' +
                        ", goods_norms='" + goods_norms + '\'' +
                        ", goods_price='" + goods_price + '\'' +
                        ", goods_cost='" + goods_cost + '\'' +
                        ", goods_discount='" + goods_discount + '\'' +
                        ", goods_type=" + goods_type +
                        ", goods_after_sale='" + goods_after_sale + '\'' +
                        ", order_goods_index='" + order_goods_index + '\'' +
                        ", subsidy='" + subsidy + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "MallOrderLists{" +
                    "order_index='" + order_index + '\'' +
                    ", order_id='" + order_id + '\'' +
                    ", order_name='" + order_name + '\'' +
                    ", filiale_id='" + filiale_id + '\'' +
                    ", region_id='" + region_id + '\'' +
                    ", supplier_id='" + supplier_id + '\'' +
                    ", order_status=" + order_status +
                    ", order_time='" + order_time + '\'' +
                    ", order_coupon='" + order_coupon + '\'' +
                    ", order_goods_price='" + order_goods_price + '\'' +
                    ", order_freight_price='" + order_freight_price + '\'' +
                    ", order_total_price='" + order_total_price + '\'' +
                    ", order_type='" + order_type + '\'' +
                    ", goods_list=" + goods_list +
                    '}';
        }
    }
}

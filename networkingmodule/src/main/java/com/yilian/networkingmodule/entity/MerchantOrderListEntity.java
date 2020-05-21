package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by LYQ on 2017/9/29.
 */

public class MerchantOrderListEntity extends HttpResultBean {


    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * order_index : 2247
         * order_id : 2017092711051492057
         * order_type : 0
         * order_status : 0
         * order_address : 收货人地址
         * cancel_time : 1506482701
         * order_phone : 7626187
         * order_contacts : 1506482701
         * order_supplier : 99
         * order_time : 1506481514
         * order_total_price : 900
         * order_goods_price : 0
         * order_supplier_price : 0
         * order_integral_price : 7000
         * order_supplier_integral : 6650
         * order_freight_price : 900
         * goods_num :
         * order_bonus : 0
         * order_province : 11
         * order_city : 149
         * order_county : 1254
         * profit_status : 0
         * cash_status : 0
         * order_source :
         * receive_order_time : 0
         * remind_time : 0
         * del_time : 0
         * is_del : 0
         * stock_time : 0
         * platform : 0
         * third_order_id : 0
         * merchant_type : 0
         * flag : 0
         * order_name : test 测试用
         * goods_list : [{"goods_name":"测试","goods_discount":0,"goods_type":"0","goods_icon":"supplier/images/664810e5b7d6cbb333c79615d219007d1dedbe3f_1.png","return_integral":0,"type":"1","goods_norms":"","goods_after_sale":"0","goods_status":"0","order_goods_index":"2416","goods_id":"2073","goods_count":"1","goods_sku":"0:0","goods_price":"0","goods_cost":"0","goods_retail":"0","goods_integral":"7000"}]
         * filiale_id : 1
         * supplier_id : 99
         */
        @SerializedName("order_quan_price")
        public String daiGouQuanMoney;
        @SerializedName("profit")
        public int profitType;//针对已完成订单   0未发奖励  1已送奖券
        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_type")
        public String orderType;
        @SerializedName("order_status")
        public String orderStatus;//订单状态0已取消，1待支付 2待出库（待备货）3正在出库（点击完成备货按钮/待发货） 4已部分发货5已全部发货&待收货6已完成
        @SerializedName("order_address")
        public String orderAddress;
        @SerializedName("cancel_time")
        public String cancelTime;
        @SerializedName("order_phone")
        public String orderPhone;
        @SerializedName("order_contacts")
        public String orderContacts;
        @SerializedName("order_supplier")
        public String orderSupplier;
        @SerializedName("order_time")
        public String orderTime;
        @SerializedName("order_total_price")
        public String orderTotalPrice;
        @SerializedName("return_bean")
        public String returnean;//获赠算力
         @SerializedName("order_total_bean")
        public String orderTotalBean;//总益豆
        @SerializedName("order_goods_price")
        public String orderGoodsPrice;
        @SerializedName("order_supplier_price")
        public String orderSupplierPrice;
        @SerializedName("order_integral_price")
        public String orderIntegralPrice;
        @SerializedName("order_supplier_integral")
        public String orderSupplierIntegral;
        @SerializedName("order_freight_price")
        public String orderFreightPrice;
        @SerializedName("goods_num")
        public String goodsNum;
        @SerializedName("order_bonus")
        public String orderBonus;
        @SerializedName("order_province")
        public String orderProvince;
        @SerializedName("order_city")
        public String orderCity;
        @SerializedName("order_county")
        public String orderCounty;
        @SerializedName("profit_status")
        public String profitStatus;
        @SerializedName("cash_status")
        public String cashStatus;
        @SerializedName("order_source")
        public String orderSource;
        @SerializedName("receive_order_time")
        public String receiveOrderTime;
        @SerializedName("remind_time")
        public String remindTime;
        @SerializedName("del_time")
        public String delTime;
        @SerializedName("is_del")
        public String isDel;
        @SerializedName("stock_time")
        public String stockTime;
        @SerializedName("platform")
        public String platform;
        @SerializedName("third_order_id")
        public String thirdOrderId;
        @SerializedName("merchant_type")
        public String merchantType;
        @SerializedName("flag")
        public int flag;
        @SerializedName("order_name")
        public String orderName;
        @SerializedName("filiale_id")
        public int filialeId;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("goods_index_str")
        public String goodsIndexStr;//整单发货时候用到该字段  未发货订单的index集合
        @SerializedName("goods_list")
        public List<GoodsListBean> goodsList;

        public static class GoodsListBean {
            /**
             * goods_name : 测试
             * goods_discount : 0
             * goods_type : 0
             * goods_icon : supplier/images/664810e5b7d6cbb333c79615d219007d1dedbe3f_1.png
             * return_integral : 0
             * type : 1
             * goods_norms :
             * goods_after_sale : 0
             * goods_status : 0
             * order_goods_index : 2416
             * goods_id : 2073
             * goods_count : 1
             * goods_sku : 0:0
             * goods_price : 0
             * goods_cost : 0
             * goods_retail : 0
             * goods_integral : 7000
             */

            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("goods_discount")
            public int goodsDiscount;
            @SerializedName("goods_type")
            public String goodsType;
            @SerializedName("goods_icon")
            public String goodsIcon;
            @SerializedName("return_integral")
            public int returnIntegral;
            @SerializedName("type")
            public String type;
            @SerializedName("goods_norms")
            public String goodsNorms;
            @SerializedName("goods_after_sale")
            public String goodsAfterSale;
            @SerializedName("goods_status")
            public String goodsStatus;
            @SerializedName("order_goods_index")
            public String orderGoodsIndex;
            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("goods_count")
            public String goodsCount;
            @SerializedName("goods_sku")
            public String goodsSku;
            @SerializedName("goods_price")
            public String goodsPrice;
            @SerializedName("goods_cost")
            public String goodsCost;
            @SerializedName("goods_retail")
            public String goodsRetail;
            @SerializedName("goods_integral")
            public String goodsIntegral;
        }
    }
}

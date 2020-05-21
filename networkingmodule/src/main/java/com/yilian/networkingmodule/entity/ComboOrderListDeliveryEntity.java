package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2017/8/15 0015.
 */

public class ComboOrderListDeliveryEntity extends HttpResultBean {

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 131
         * merchant_id : 1403
         * merchant_type : 0
         * order_number : 2017081616400376544
         * package_id : 7
         * package_name : 卷馒头
         * type : 2
         * buy_date : 1502872803
         * user_id : 6600882238224802
         * price : 1000
         * retail_price : 200
         * amount : 1000
         * merchant_amount : 200
         * delivery_cost : 0
         * status : 1 //1未使用待接单 2待配送 3配送中 4已使用已送达 6取消订单
         * is_profit : 1
         * is_evaluate : 0
         * order_id : 0
         * pay_date : 1502872806
         * is_del : 0
         * del_time : 0
         * pay_type : 0
         * delivery_date : 1502872803
         * receive_order_date : 0
         * delivery_done_date : 0
         * phone : 15238664340
         * name : 暨南
         * linkphone : 15238664345
         * address : 河南省郑州市管城回族区世正商·建正东方中心顺流逆流弄
         * location : 34.745469,113.776984
         * content : [{"package_name":"食品","content":[{"name":"卷馒头","number":"1","cost":"10"}]}]
         * ids : 131
         * time : 1502872803
         * package_icon : supplier/images/d143a7f70f8e43bc292159599698fce55a05a80d_18516875325369360.gif
         * all_coupon : [{"couId":"91","coupon":"137033","verify_date":"0","status":"1"}]
         * num : 1
         */

        @SerializedName("ids")
        public String comboOrderId;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_type")
        public String merchantType;
        @SerializedName("order_number")
        public String orderNumber;
        @SerializedName("package_id")
        public String packageId;
        @SerializedName("package_name")
        public String packageName;
        @SerializedName("type")
        public String type;
        @SerializedName("buy_date")
        public String buyDate;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("price")
        public String price;
        @SerializedName("retail_price")
        public String retailPrice;
        @SerializedName("amount")
        public String amount;
        @SerializedName("merchant_amount")
        public String merchantAmount;
        @SerializedName("delivery_cost")
        public String deliveryCost;
        //1未使用待接单 2待配送 3配送中 4已使用已送达 6取消订单
        @SerializedName("status")
        public int status;
        @SerializedName("is_profit")
        public String isProfit;
        @SerializedName("is_evaluate")
        public String isEvaluate;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("pay_date")
        public String payDate;
        @SerializedName("is_del")
        public String isDel;
        @SerializedName("del_time")
        public String delTime;
        @SerializedName("pay_type")
        public String payType;
        @SerializedName("delivery_date")
        public String deliveryDate;
        @SerializedName("receive_order_date")
        public String receiveOrderDate;
        @SerializedName("delivery_done_date")
        public String deliveryDoneDate;
        @SerializedName("phone")
        public String phone;
        @SerializedName("name")
        public String name;
        @SerializedName("linkphone")
        public String linkphone;
        @SerializedName("address")
        public String address;
        @SerializedName("location")
        public String location;
        @SerializedName("content")
        public String content;
        @SerializedName("time")
        public long time;
        @SerializedName("package_icon")
        public String packageIcon;
        @SerializedName("num")
        public int num;
        @SerializedName("all_coupon")
        public List<AllCouponBean> allCoupon;

        public static class AllCouponBean {
            /**
             * couId : 91
             * coupon : 137033
             * verify_date : 0
             * status : 1
             */

            @SerializedName("couId")
            public String couId;
            @SerializedName("coupon")
            public String coupon;
            @SerializedName("verify_date")
            public String verifyDate;
            @SerializedName("status")
            public String status;
        }
    }
}

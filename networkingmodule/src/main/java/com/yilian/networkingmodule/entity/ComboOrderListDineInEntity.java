package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2017/8/15 0015.
 */

public class ComboOrderListDineInEntity extends HttpResultBean {

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 126
         * merchant_id : 1403
         * merchant_type : 0
         * order_number : 2017081616225692283
         * package_id : 7
         * package_name : 卷馒头
         * type : 1
         * buy_date : 1502871776
         * user_id : 6646669267210800
         * price : 1000
         * retail_price : 200
         * amount : 1000
         * merchant_amount : 200
         * delivery_cost : 0
         * status : 1
         * is_profit : 1
         * is_evaluate : 0
         * order_id : 0
         * pay_date : 1502871779
         * is_del : 0
         * del_time : 0
         * pay_type : 0
         * phone : 18336620513
         * content : [{"package_name":"食品","content":[{"name":"卷馒头","number":"1","cost":"10"}]}]
         * ids : 126
         * time : 1502871776
         * package_icon : supplier/images/d143a7f70f8e43bc292159599698fce55a05a80d_18516875325369360.gif
         * all_coupon : [{"couId":"86","coupon":"176300","verify_date":"0","status":"1"}]
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
        public long buyDate;
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
        @SerializedName("status")
        public String status;
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
        @SerializedName("phone")
        public String phone;
        @SerializedName("content")
        public String content;
        @SerializedName("time")
        public String time;
        @SerializedName("package_icon")
        public String packageIcon;
        @SerializedName("num")
        public int num;
        @SerializedName("all_coupon")
        public List<AllCouponBean> allCoupon;

        public static class AllCouponBean {
            /**
             * couId : 86
             * coupon : 176300
             * verify_date : 0
             * status : 1
             */

            @SerializedName("couId")
            public String couId;
            @SerializedName("coupon")
            public String coupon;
            @SerializedName("verify_date")
            public String verifyDate;
            //1未兑换 2已兑换 3待退款 4已退款
            @SerializedName("status")
            public int status;
        }
    }
}

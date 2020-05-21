package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/4/25 0025.
 */

public class MallFlashSalePayOrderEntity extends BaseEntity {


    /**
     * data : {"order_index":"7164","order_total_voucher":"5400","order_total_coupon":0,"order_total_lebi":"1800.0"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * order_index : 7164
         * order_total_voucher : 5400
         * order_total_coupon : 0
         * order_total_lebi : 1800.0
         */
        //****************************总部限时购提交订单*************************************
        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("order_total_voucher")
        public String orderTotalVoucher;
        @SerializedName("order_total_coupon")
        public int orderTotalCoupon;
        @SerializedName("order_total_lebi")
        public String orderTotalLebi;


        //--------------------------------总部限时购支付------------------------------------------------------

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("lebi")
        public String lebi;
        @SerializedName("coupon")
        public String coupon;
        @SerializedName("voucher")
        public String voucher;
        @SerializedName("deal_time")
        public String deal_time;
        @SerializedName("intergral")
        public String integral;
        @SerializedName("give_voucher")
        public String give_voucher;


    }
}

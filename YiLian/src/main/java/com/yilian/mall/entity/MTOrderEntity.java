package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/12/7 0007.
 * 美团-订单详情实体类
 */

public class MTOrderEntity extends BaseEntity {
    @SerializedName("supply_status")
    public int supplyStatus;// 1标识申请发奖励显示  0标识申请发奖励不显示（申请发奖励到店消费订单才有）
    @SerializedName("profits")
    public int profitStatus;//0未发奖励 1已送奖券
    @SerializedName("name")
    public String name;

    @SerializedName("codes")
    public ArrayList<MTCodesEntity> codes;

    @SerializedName("price")
    public String price;

    @SerializedName("end_time")
    public String endTime;

    @SerializedName("full_minus_fee")   //满减
    public String fullMinusFee;

    @SerializedName("status")//套餐状态 0待付款 1未使用待接单 2待配送 3配送中 4已使用已送达 5已退款 6已取消
    public String status;

    @SerializedName("package_id")
    public String packageId;

    @SerializedName("package_icon")
    public String packageIcon;

    @SerializedName("is_evaluate")
    public String isEvaluate;

    @SerializedName("merchant_info")
    public MerchantInfo merchantInfo;

    @SerializedName("return_integral")
    public String returnIntegral;//返回的可得奖券字段

    public class MerchantInfo {
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_desp")
        public String merchantDesp;
        @SerializedName("merchant_longitude")//商家地理位置经度
        public String merchantLongitude;
        @SerializedName("merchant_latitude")//商家地理位置纬度
        public String merchantLatitude;
        @SerializedName("merchant_address")//商家位置信息
        public String merchantAddress;
        @SerializedName("merchant_tel")//商家联系方式
        public String merchantTel;
        @SerializedName("is_delivery")
        public String isDelivery;  //是否是配送订单 0不配送  1配送
    }

    @SerializedName("package_info")
    public ArrayList<PackageInfo> packageInfo;

    public class PackageInfo {
        @SerializedName("package_name")
        public String packageName;

        @SerializedName("content")
        public ArrayList<Content> content;

        public class Content {
            @SerializedName("name")
            public String name;

            @SerializedName("number")
            public String number;

            @SerializedName("cost")
            public String cost;
        }
    }

    @SerializedName("order_info")
    public OrderInfo orderInfo;

    public class OrderInfo {
        @SerializedName("contact_name")
        public String contactName;
        @SerializedName("contact_phone")
        public String contactPhone;
        @SerializedName("contact_address")
        public String contactAddress;
        @SerializedName("type") //1 到店消费    2 订单外卖
        public String type;

        @SerializedName("order_number")//订单号
        public String orderNumber;

        @SerializedName("phone")
        public String phone;

        @SerializedName("pay_time")
        public String payTime;

        @SerializedName("count")
        public String count;

        @SerializedName("amount")
        public String amount;

        @SerializedName("profit") //鼓励金 0表示没有
        public String profit;

        @SerializedName("server_time") //系统时间戳
        public String serverTime;

        @SerializedName("verify_date")  //商家接单时间戳
        public String verifyDate;
    }

}

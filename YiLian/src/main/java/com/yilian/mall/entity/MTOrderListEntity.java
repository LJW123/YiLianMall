package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/12/7 0007.
 * 美团-订单列表实体类
 */

public class MTOrderListEntity extends BaseEntity {

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public class DataBean implements Serializable {
        @SerializedName("package_id")
        public String packageId;
        @SerializedName("package_name")
        public String packageName;
        @SerializedName("price")
        public String price;
        @SerializedName("status") //套餐状态 0待付款 1未使用待接单 2待配送 3配送中 4已使用已送达 5已退款 6已取消
        public String status;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("package_icon")
        public String packageIcon;
        @SerializedName("usable_time")
        public String usableTime;
        @SerializedName("count")
        public String count;
        @SerializedName("is_delivery") //1配送，0不配送
        public String isDelivery;
        @SerializedName("codes")
        public ArrayList<MTCodesEntity> codes;
        @SerializedName("is_evaluate") //0未评价 1已经评价
        public String isEvaluate;
        @SerializedName("order_id")
        public String orderId;

        @Override
        public String toString() {
            return "ListBean{" +
                    "packageId='" + packageId + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", price='" + price + '\'' +
                    ", status='" + status + '\'' +
                    ", merchantName='" + merchantName + '\'' +
                    ", packageIcon='" + packageIcon + '\'' +
                    ", usableTime='" + usableTime + '\'' +
                    ", count='" + count + '\'' +
                    ", isDelivery='" + isDelivery + '\'' +
                    ", codes=" + codes +
                    ", isEvaluate='" + isEvaluate + '\'' +
                    ", orderId='" + orderId + '\'' +
                    '}';
        }
    }

}

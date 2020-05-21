package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yukun on 2016/4/1.
 */
public class BarterAfterSaleRouing extends BaseEntity {

    public Status status;

    public class Status {

        @SerializedName("barter_status")//订单状态：0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        public int status;
        @SerializedName("barter_time")//申请时间
        public String barterTime;
        @SerializedName("check_time")//审核时间
        public String checkTime;


        @SerializedName("barter_express_time")//用户发货时间
        public String barterExpressTime;
        @SerializedName("merchant_confirm_time")//商家确认收货时间
        public String merchantConfirmTime;
        @SerializedName("merchant_express_time")//用户发货时间
        public String merchantExpressTime;
        @SerializedName("consumer_confirm_time")//用户确认收货时间
        public String consumerConfirmTime;

    }
}

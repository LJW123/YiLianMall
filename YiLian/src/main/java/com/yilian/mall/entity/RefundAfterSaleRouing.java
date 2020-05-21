package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yukun on 2016/4/1.
 */
public class RefundAfterSaleRouing extends BaseEntity {

    public Status status;

    public class Status {

        @SerializedName("refund_status")//订单状态：0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成
        public int status;
        @SerializedName("refund_time")//申请时间
        public String refundTime;
        @SerializedName("check_time")//审核时间
        public String checkTime;
        @SerializedName("express_time")//发货时间
        public String expressTime;
        @SerializedName("confirm_time")//确认收货时间
        public String confirmTime;
        @SerializedName("payment_time")//退款完成时间
        public String paymentTime;

    }
}

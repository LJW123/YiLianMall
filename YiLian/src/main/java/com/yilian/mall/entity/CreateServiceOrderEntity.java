package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/2/21.
 */
public class CreateServiceOrderEntity extends BaseEntity {

    public String time;//退货申请时间

    @SerializedName("service_order")
    public String serviceOrder; // 售后订单编号

    @SerializedName("service_index")
    public String serviceIndex; //

    @SerializedName("order_id")
    public String orderId; // 订单编号
}

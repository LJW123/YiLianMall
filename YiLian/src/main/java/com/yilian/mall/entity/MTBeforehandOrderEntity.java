package com.yilian.mall.entity;/**
 * Created by  on 2016/12/26 0026.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2016/12/26 0026.
 * 套餐 商家预支付订单
 */
public class MTBeforehandOrderEntity extends BaseEntity {

    /**
     * order_id :
     */

    @SerializedName("order_id")
    public String orderId;
}

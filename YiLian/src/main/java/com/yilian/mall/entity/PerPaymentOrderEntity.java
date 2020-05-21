package com.yilian.mall.entity;/**
 * Created by  on 2016/12/29 0029.
 */

import com.google.gson.annotations.SerializedName;

/**生成预支付订单
 * Created by  on 2016/12/29 0029.
 */
public class PerPaymentOrderEntity extends BaseEntity {

    /**
     * order_id :
     */

    @SerializedName("order_id")
    public String orderId;
}

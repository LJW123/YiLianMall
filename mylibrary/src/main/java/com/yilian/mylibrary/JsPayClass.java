package com.yilian.mylibrary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2017/6/23 0023.
 * JS拉起微信或支付宝时传递的订单信息Json对应的实体类
 */

public class JsPayClass extends BaseEntity implements Serializable {
    public JsPayClass(String orderString, String order_index, String payment_apply_time, String payment_fee, String paymentIndex) {
        this.orderString = orderString;
        this.order_index = order_index;
        this.payment_apply_time = payment_apply_time;
        this.payment_fee = payment_fee;
        this.paymentIndex = paymentIndex;
    }

    /**
     * {
     * orderString = "";
     * "order_index" = 327;
     * "payment_apply_time" = 1498027130;
     * "payment_fee" = "0.01";
     * "paymentIndex" = 2017062114385037463;
     * }
     */
    @SerializedName("orderString")
    public String orderString;//使用支付宝支付时，系统返回的支付宝订单信息
    @SerializedName("order_index")
    public String order_index;//订单ID
    @SerializedName("payment_apply_time")
    public String payment_apply_time;//
    @SerializedName("payment_fee")
    public String payment_fee;
    @SerializedName("paymentIndex")
    public String paymentIndex;

}

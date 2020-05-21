package com.yilian.networkingmodule.entity;

import java.io.Serializable;

/**
 * Created by  on 2017/6/23 0023.
 * JS拉起微信或支付宝时传递的订单信息Json对应的实体类
 */

public class JsPayClass implements Serializable{
    /**
     * {
     * orderString = "";
     * "order_index" = 327;
     * "payment_apply_time" = 1498027130;
     * "payment_fee" = "0.01";
     * "paymentIndex" = 2017062114385037463;
     * }
     */
    public String orderString;//使用支付宝支付时，系统返回的支付宝订单信息
    public String order_index;//订单ID
    public String payment_apply_time;//
    public String payment_fee;
    public String payment_index;

}

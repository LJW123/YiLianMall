package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/5/28.
 */

public class JDThirdPayPreRechargeOrderEntity extends HttpResultBean {

    /**
     * orderString : 1
     * payment_index : 2018052211472196736 支付交易流水号
     * payment_apply_time : 1526960841 支付时间
     * payment_fee : 100 支付金额
     * order_index : 21 支付订单号 短的
     */

    @SerializedName("orderString")
    public String orderString;
    @SerializedName("payment_index")
    public String paymentIndex;
    @SerializedName("payment_apply_time")
    public int paymentApplyTime;
    @SerializedName("payment_fee")
    public String paymentFee;
    @SerializedName("order_index")
    public String orderIndex;
}

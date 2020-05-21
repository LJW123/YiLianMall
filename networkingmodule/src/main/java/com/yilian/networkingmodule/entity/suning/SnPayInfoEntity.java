package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author Created by  on 2018/5/28.
 */

public class SnPayInfoEntity extends HttpResultBean implements Serializable{


    /**
     * total_cash : 17.9
     * freight : 5.00
     * return_bean : 0.03
     * coupon : 10.00
     * orderSnPrice : 12.90
     * orderPrice : 12.84
     * pay_type : 12
     * order_id : 2018080310000963032
     * order_index : 102
     * sn_order_id : 100002102080
     * paymentType : 5
     * paymentTypeStr : 奖励支付
     */

    @SerializedName("total_cash")
    public double totalCash;
    @SerializedName("freight")
    public String freight;
    @SerializedName("return_bean")
    public String returnBean;
    @SerializedName("coupon")
    public String coupon;
    @SerializedName("orderSnPrice")
    public String orderSnPrice;
    @SerializedName("orderPrice")
    public String orderPrice;
    @SerializedName("pay_type")
    public int payType;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("sn_order_id")
    public String snOrderId;
    @SerializedName("paymentType")
    public int paymentType;
    @SerializedName("paymentTypeStr")
    public String paymentTypeStr;
}

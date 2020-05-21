package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author Created by  on 2018/5/28.
 */

public class CtripPayInfoEntity extends HttpResultBean implements Serializable{


    /**
     * total_cash : 333.00
     * return_bean : 16.65
     * pay_type : 14
     * order_id : 3064713298
     * order_index : 25
     * paymentType : 5
     * paymentTypeStr : 奖励支付
     */

    @SerializedName("total_cash")
    public String totalCash;
    @SerializedName("return_bean")
    public String returnBean;
    @SerializedName("pay_type")
    public int payType;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("paymentType")
    public int paymentType;
    @SerializedName("paymentTypeStr")
    public String paymentTypeStr;

}

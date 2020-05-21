package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/11/14 0014
 */

public class GALMakeOrderEntity extends HttpResultBean {

    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("done_time")
    public String doneTime;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("order_integral")
    public double orderIntegral;

    @Override
    public String toString() {
        return "GALMakeOrderEntity{" +
                "orderIndex='" + orderIndex + '\'' +
                ", doneTime='" + doneTime + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderIntegral='" + orderIntegral + '\'' +
                '}';
    }
}

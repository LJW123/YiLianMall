package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/8/18 0018.
 */

public class UpdateComboStatusEntity extends HttpResultBean {
    @SerializedName("orderId")
    public String orderId;
    @SerializedName("orderStatus")
    public int orderStatus;
}

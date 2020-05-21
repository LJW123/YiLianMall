package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/8/5 0005.
 */

public class RetailPayEntity extends BaseEntity{
    @SerializedName("total_cash")
    public String totalCash;//总金额
    @SerializedName("profit_cash")
    public String profitCash;//让利金额
    @SerializedName("order_index")
    public String orderIndex;//生成的订单号
}

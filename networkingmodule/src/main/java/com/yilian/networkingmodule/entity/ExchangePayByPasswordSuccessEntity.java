package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author xiaofo on 2018/9/8.
 */

public class ExchangePayByPasswordSuccessEntity extends HttpResultBean {

    /**
     * lebi :
     * total_quan :
     * deal_time :
     * order_id :
     */

    @SerializedName("lebi")
    public String lebi;
    @SerializedName("total_quan")
    public String totalQuan;
    @SerializedName("deal_time")
    public String dealTime;
    @SerializedName("order_id")
    public String orderId;
}

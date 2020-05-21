package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Administrator on 2016/9/10.
 */
public class PayOkEntity extends HttpResultBean {


    /**
     * lebi : 9400
     * return_bean : 2213
     * deal_time : 1531060110
     * order_id : 37706
     * subsidy : 0
     */

    @SerializedName("lebi")
    public String lebi;
    @SerializedName("return_bean")
    public String returnBean;
    @SerializedName("deal_time")
    public String dealTime;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("subsidy")
    public String subsidy;
}

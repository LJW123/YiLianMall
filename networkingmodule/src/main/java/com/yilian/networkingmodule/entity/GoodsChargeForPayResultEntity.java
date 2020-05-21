package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2016/12/27 0027.
 */

public class GoodsChargeForPayResultEntity extends BaseEntity {


    /**
     * lebi : 9400
     * order_id : 37708
     * deal_time : 1531061738
     * return_bean : 2213
     * subsidy : 0
     * merchant_name : false
     * merchant_phone : ****
     */
    @SerializedName("order_quan")
    public String daiGouQuanMoney;
    @SerializedName("lebi")
    public String lebi;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("deal_time")
    public String dealTime;
    @SerializedName("return_bean")
    public String returnBean;
    @SerializedName("subsidy")
    public String subsidy;
    @SerializedName("merchant_name")
    public String merchantName;
    @SerializedName("merchant_phone")
    public String merchantPhone;
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * Created by LYQ on 2017/10/13.
 */

public class AfterSaleBtnClickResultEntity extends HttpResultBean implements Serializable {
    @SerializedName("status")
    public String status;
    @SerializedName("type")
    public String type;
    @SerializedName("barter_type")
    public String barterType;
    @SerializedName("merchant_express_company")
    public String merchantExpressCompany;//卖家退货公司简称
    @SerializedName("merchant_express_number")
    public String merchantExpressNumber;//卖家退货单号

}

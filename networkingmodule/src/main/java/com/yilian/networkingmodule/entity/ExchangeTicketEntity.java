package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * 接受转赠条件
 * Created by Zg on 2018/7/21
 */
public class ExchangeTicketEntity extends HttpResultBean implements Serializable {

    /**
     * quan : 190665
     * income : 190665
     * expend : 0
     * cart_num : "20" //清单总数
     */

    @SerializedName("quan")
    public String quan;
    @SerializedName("income")
    public String income;
    @SerializedName("expend")
    public String expend;
    @SerializedName("cart_num")
    public String cartNum;
    @SerializedName("quan_transfer_charge_rate")
    public String rate;
    @SerializedName("lower_quan_for_transfer")
    public String minValue;

}

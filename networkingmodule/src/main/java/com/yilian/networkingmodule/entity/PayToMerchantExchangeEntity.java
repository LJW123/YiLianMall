package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * @author xiaofo on 2018/9/7.
 */

public class PayToMerchantExchangeEntity extends HttpResultBean implements Serializable{

    /**
     * exchange_type : 兑换方式
     * exchange_time : 兑换时间，时间戳
     * exchange_quan : 兑换券数量
     */

    @SerializedName("exchange_type")
    public String exchangeType;
    @SerializedName("exchange_time")
    public long exchangeTime;
    @SerializedName("exchange_quan")
    public String exchangeQuan;
}

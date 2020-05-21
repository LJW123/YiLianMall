package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;


/**
 * 我的购物余额
 * Created by Zg on 2018/11/18
 */
public class MyCardEntity extends HttpResultBean {
    /**
     * //购物卡余额 分
     */
    @SerializedName("card_amount")
    public String cardAmount;

}

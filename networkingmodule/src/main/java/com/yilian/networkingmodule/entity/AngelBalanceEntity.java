package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 商家天使首页
 * Created by Zg on 2018/9/8
 */
public class AngelBalanceEntity extends HttpResultBean  {

    /**
     * user_angel : 0.00000000
     * income_angel : 0
     * expend_angel : 0
     */

    @SerializedName("user_angel")
    public String userAngel;
    @SerializedName("income_angel")
    public String incomeAngel;
    @SerializedName("expend_angel")
    public String expendAngel;


}

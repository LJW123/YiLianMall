package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by liuyuqi on 2017/6/8 0008.
 */
public class TakeLedouSuccessEntity extends HttpResultBean {


    /**
     * bean : 0
     * total_bean : 4000
     * charge_rate : 2
     */

    @SerializedName("actual_amount")
    public String actualAmount;
}

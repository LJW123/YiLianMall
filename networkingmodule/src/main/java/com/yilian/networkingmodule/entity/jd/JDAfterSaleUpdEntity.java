package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 京东更新售后订单
 *
 * @author Created by Zg on 2018/5/28.
 */

public class JDAfterSaleUpdEntity extends HttpResultBean {

    @SerializedName("afsServiceId")
    public String afsServiceId;
}

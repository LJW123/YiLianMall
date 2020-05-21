package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 京东政企判断订单中的商品是否可以申请售后
 *
 * @author Created by Zg on 2018/5/28.
 */

public class JDCheckAvailableAfterSaleEntity extends HttpResultBean {

    @SerializedName("num")
    public int num;
}

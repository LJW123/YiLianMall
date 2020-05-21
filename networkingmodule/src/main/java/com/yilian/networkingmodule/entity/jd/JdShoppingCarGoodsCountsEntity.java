package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 京东购物车商品数量
 *
 * @author Created by zhaiyaohua on 2018/6/29.
 */

public class JdShoppingCarGoodsCountsEntity extends HttpResultBean {
    @SerializedName("list")
    public double list;
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * 我的营收实体类
 *
 * @author Created by zhaiyaohua on 2018/7/5.
 */

public class MerchantMyRevenueEntity extends HttpResultBean {

    /**
     * money : 0
     * total_money : 0
     * extract_money : 0
     * is_cert : 0没有实名 1已经实名
     */

    @SerializedName("money")
    public String money;
    @SerializedName("total_money")
    public String totalMoney;
    @SerializedName("extract_money")
    public String extractMoney;
    @SerializedName("is_cert")
    public int isCert;
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/10/14 0014.
 * 商家发货成功后的实体类
 */

public class MerchantDeliverGoods extends HttpResultBean {
    @SerializedName("status")
    public int status;
}

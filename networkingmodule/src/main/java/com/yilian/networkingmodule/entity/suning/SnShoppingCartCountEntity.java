package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author xiaofo on 2018/7/25.
 */

public class SnShoppingCartCountEntity extends HttpResultBean {


    /**
     * count : 1
     */

    @SerializedName("count")
    public int count;
}

package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author xiaofo on 2018/7/25.
 */

public class SnFreightEntity extends HttpResultBean {

    /**
     * freight :
     */

    @SerializedName("freight")
    public String freight;
}

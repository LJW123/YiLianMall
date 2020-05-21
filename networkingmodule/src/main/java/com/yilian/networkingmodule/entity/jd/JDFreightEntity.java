package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/5/22.
 */

public class JDFreightEntity extends HttpResultBean {


    /**
     * freight : 8
     * 运费
     */

    @SerializedName("freight")
    public float freight;
}

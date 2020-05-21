package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/5/29.
 */

public class JDGoodsStoreEntity extends HttpResultBean {

    /**
     * stockStateId : 33
     * stockStateDesc : 有货
     * remainNum : 1
     */

    @SerializedName("stockStateId")
    public int stockStateId;
    @SerializedName("stockStateDesc")
    public String stockStateDesc;
    @SerializedName("remainNum")
    public int remainNum;
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by ZYH on 2017/12/5 0005.
 */

public class StealRedPackgesPushStatus extends HttpResultBean {
    @SerializedName("type")
    public String type;

}

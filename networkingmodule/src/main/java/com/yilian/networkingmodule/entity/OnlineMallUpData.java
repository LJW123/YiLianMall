package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by ${zhaiyaohua} on 2017/12/29 0029.
 * @author zhaiyaohua
 */

public class OnlineMallUpData extends HttpResultBean {


    /**
     * code : 1
     * str :---这个是头部的内容
     * url :
     */
    @SerializedName("str")
    public String str;
    @SerializedName("url")
    public String url;
}

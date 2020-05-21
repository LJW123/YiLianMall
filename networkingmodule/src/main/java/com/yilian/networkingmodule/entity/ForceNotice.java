package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/9/7 0007.
 */

public class ForceNotice extends HttpResultBean {

    /**
     * title :
     * content :
     * company :
     * time :
     */

    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("company")
    public String company;
    @SerializedName("time")
    public String time;
}

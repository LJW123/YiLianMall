package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2018/2/5 0005.
 */

public class MerchantItemBeanEntity {

    /**
     * title : 待接单
     * img : /app/images/icon/20180205/icon_daijiedan.png
     * type : 34
     * content : 1
     * count : 0
     * content_type : 0
     */

    @SerializedName("title")
    public String title;
    @SerializedName("img")
    public String img;
    @SerializedName("type")
    public int type;
    @SerializedName("content")
    public String content;
    @SerializedName("count")
    public String count;
    @SerializedName("content_type")
    public int contentType;
}

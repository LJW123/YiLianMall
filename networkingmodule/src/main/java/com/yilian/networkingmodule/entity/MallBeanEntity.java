package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/12/21 0021.
 */

public class MallBeanEntity {

    @SerializedName("title")
    public String title;
    @SerializedName("img")
    public String img;
    @SerializedName("count")
    public String count;
    @SerializedName("type")
    public int type;
    @SerializedName("content")
    public String content;
    @SerializedName("content_type")
    public String contentType;
    @SerializedName("num")
    public String num;

}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/2/23 0023.
 */

public class AliMessageEntity {

    /**
     * address : 郑州市中原区须水镇二砂村204号
     * birth : 19930307
     * config_str : {"side":"face"}
     * name : 刘玉琦
     * nationality : 汉
     * num : 410102199303070141
     * request_id : 20170223104624_9f7c89ec58934527ec91b12564d5316b
     * sex : 女
     * success : true
     */

    @SerializedName("address")
    public String address;
    @SerializedName("birth")
    public String birth;
    @SerializedName("config_str")
    public String configStr;
    @SerializedName("name")
    public String name;
    @SerializedName("nationality")
    public String nationality;
    @SerializedName("num")
    public String num;
    @SerializedName("request_id")
    public String requestId;
    @SerializedName("sex")
    public String sex;
    @SerializedName("success")
    public boolean success;
}

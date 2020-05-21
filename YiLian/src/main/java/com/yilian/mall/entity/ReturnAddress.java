package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 刘玉坤 on 2016/3/7.
 */
public class ReturnAddress {

    public String contact; // 联系人

    public String tel; // 联系人电话

    public String address; // 联系人地址

    @SerializedName("post_code")
    public String postCode;
}

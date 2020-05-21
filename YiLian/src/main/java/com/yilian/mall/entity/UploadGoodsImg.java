package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yukun on 2016/3/31.
 */
public class UploadGoodsImg extends BaseEntity {
    @SerializedName("filename")
    public String fileName;

    @SerializedName("message")
    public String message;
}

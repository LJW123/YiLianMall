package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/5/30.
 */
public class ShareRecordEntity extends BaseEntity {

    @SerializedName("filename")
    public String filename;
    @SerializedName("message")
    public String message;
}

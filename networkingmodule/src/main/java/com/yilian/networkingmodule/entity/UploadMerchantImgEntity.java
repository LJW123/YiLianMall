package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/6/13 0013.
 * 商家提交认证资料上传借口
 */
public class UploadMerchantImgEntity extends BaseEntity {

    @SerializedName("“filename”")
    public String filename;
    @SerializedName("“message”")
    public String message;
}

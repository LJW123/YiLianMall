package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/6/15 0015.
 */

public class MerchantQrCodeEntity extends BaseEntity {

    /**
     * qrCodeContent :
     * merchant_name : 豪爽来
     * merchant_percent : 80
     * merchant_img : app/images/head.jpg
     * filename :
     */

    @SerializedName("qrCodeContent")
    public String qrCodeContent;
    @SerializedName("merchant_name")
    public String merchantName;//名字
    @SerializedName("merchant_percent")
    public float merchantPercent;//折扣
    @SerializedName("merchant_img")
    public String merchantImg;//门头图片
    @SerializedName("filename")
    public String filename;//二维码地址
}

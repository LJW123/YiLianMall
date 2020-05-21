package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author xiaofo on 2018/9/7.
 */

public class MerchantExchangeQrCodeEntity extends HttpResultBean {

    /**
     * mer_user_id :
     * merchant_name : 豪爽来
     * merchant_img : //门头照片
     * filename ://二维码文件路径   拼接CDN地址
     */

    @SerializedName("mer_user_id")
    public String merUserId;
    @SerializedName("merchant_name")
    public String merchantName;
    @SerializedName("merchant_img")
    public String merchantImg;
    @SerializedName("filename")
    public String filename;
}

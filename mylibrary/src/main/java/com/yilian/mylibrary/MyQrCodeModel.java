package com.yilian.mylibrary;

import com.google.gson.annotations.SerializedName;

/**
 * @author xiaofo on 2018/9/7.
 */

public class MyQrCodeModel {

    /**
     * content : 具体内容
     * type : 区分类型
     */

    @SerializedName("content")
    public String content;
    @SerializedName("type")
    public String type;
   public class QrCodeType{
        public static final String MERCHANT_EXCHANGE_QR_CODE="1";
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author xiaofo on 2018/9/7.
 */

public class MerchantExchangeEntity extends HttpResultBean {

    /**
     * mer_user_id :
     * merchant_name : 豪爽来
     * merchant_img : //门头照片
     */

    @SerializedName("mer_user_id")
    public String merUserId;
    @SerializedName("merchant_name")
    public String merchantName;
    @SerializedName("merchant_img")
    public String merchantImg;
}

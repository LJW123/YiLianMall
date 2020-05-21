package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/2/8.
 */

public class AddMerchantInfoEntity extends HttpResultBean {
    @SerializedName("merchant_apply_id")
    public String merchatnApplyId;
}

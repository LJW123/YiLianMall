package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/8/18 0018.
 */

public class CancelComboEntity extends HttpResultBean {
    @SerializedName("status")
    public int orderStatus;
}

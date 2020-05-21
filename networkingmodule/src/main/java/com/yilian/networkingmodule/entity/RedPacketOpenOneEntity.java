package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/11/23 0023.
 */

public class RedPacketOpenOneEntity extends HttpResultBean {

    @SerializedName("money")
    public String money;
    @SerializedName("openTime")
    public String openTime;

}

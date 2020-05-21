package com.yilian.networkingmodule.httpresult;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/8/14 0014.
 */

public class HttpResultBean extends BaseBean {
    @SerializedName("code")
    public int code;
    @SerializedName("msg")
    public String msg;



}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/1/19.
 */

public class IsShowImgCode extends HttpResultBean {

    /**
     * is_show : //1代表直接发送短信  0代表需要弹图形验证码
     */

    @SerializedName("show")
    public int isShow;
}

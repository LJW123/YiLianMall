package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/30.
 */
public class WeiXinLoginEntity extends BaseEntity{

    @SerializedName("access_token")
    public String access_token;
    @SerializedName("expires_in")
    public String expires_in;
    @SerializedName("refresh_token")
    public String refresh_token;
    @SerializedName("openid")
    public String openid;
    @SerializedName("scope")
    public String scope;
    @SerializedName("unionid")
    public String unionid;
}

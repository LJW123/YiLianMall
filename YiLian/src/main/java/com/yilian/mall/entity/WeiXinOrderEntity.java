package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/3/31.
 */
public class WeiXinOrderEntity extends BaseEntity {

    @SerializedName("appid")
    public String appId;
    @SerializedName("partnerid")
    public String partnerId;
    @SerializedName("prepayid")
    public String prepayId;
    @SerializedName("package")
    public String packageValue;
    @SerializedName("noncestr")
    public String nonceStr;
    @SerializedName("timestamp")
    public String timeStamp;
    @SerializedName("sign")
    public String sign;
}

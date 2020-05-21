package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/30.
 */
public class WeiXinInfoEntity  extends BaseEntity{

    @SerializedName("openid")
    public String openid;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("sex")
    public String sex;
    @SerializedName("province")
    public String province;
    @SerializedName("city")
    public String city;
    @SerializedName("country")
    public String country;
    @SerializedName("headimgurl")
    public String headimgurl;
    @SerializedName("unionid")
    public String unionid;

//    @SerializedName("privilege")
//
//    public ArrayList<Privilege>  privilege;
//
//    public class Privilege{
//        @SerializedName("PRIVILEGE1")
//        public String PRIVILEGE1;
//    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ASUS on 2017/4/11 0011.
 */

public class LoginEntity implements Serializable{


    /**
     * device_index : 160
     * code : 1
     * token : 3550631554178881220
     * given_coupon : 0
     */
    @SerializedName("phone")
    public String phone;
    @SerializedName("code")
    public int code;
    @SerializedName("token")
    public String token;
    //新用户注册时获赠的现金券数量，0表示没有获得赠送现金券
    @SerializedName("given_coupon")
    public int givenCoupon;
    @SerializedName("msg")
    public String msg;
    @SerializedName("info_version")
    public String infoVersion;//用户资料版本号，用于判断是否需要重新获取用户资料
    @SerializedName("given_integral")
    public String givenIntegral;//新用户注册时获赠的奖券数量，0表示没有获得赠送奖券
    @SerializedName("integral_balance")
    public String integralBalance;//用户当前积发奖励包
    @Override
    public String toString() {
        return "LoginEntity{" +
                ", code=" + code +
                ", token='" + token + '\'' +
                ", givenCoupon=" + givenCoupon +
                '}';
    }
}

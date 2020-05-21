package com.yilian.mall.entity;/**
 * Created by  on 2017/1/9 0009.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/1/9 0009.
 */
public class WXBindPhoneEntity extends BaseEntity {

    /**
     * token ://登录凭证，登录成功时才有此字段
     * given_coupon ://新用户注册时获赠的抵扣券数量，0表示没有获得赠送抵扣券
     * info_version ://用户资料版本号，用于判断是否需要重新获取用户资料
     * integral_balance ://用户当前积发奖励包
     */

    @SerializedName("token")
    public String token;
    @SerializedName("given_coupon")
    public String givenCoupon;
    @SerializedName("info_version")
    public String infoVersion;
    @SerializedName("integral_balance")
    public String integralBalance;
}

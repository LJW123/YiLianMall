package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class ObtainIntegral extends BaseEntity {

    private String status; //赠送活动状态    0 活动未开始  1 进行中  2 暂停  3 结束 4 已经领过不能再领
    private String given_integral; //获得奖券数量
    private String integral_balance; //用户当前积发奖励包
    private String coupon;//乐券奖励
    @SerializedName("voucher")
    public String voucher;//零购券奖励

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGiven_integral() {
        return given_integral;
    }

    public void setGiven_integral(String given_integral) {
        this.given_integral = given_integral;
    }

    public String getIntegral_balance() {
        return integral_balance;
    }

    public void setIntegral_balance(String integral_balance) {
        this.integral_balance = integral_balance;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }
}

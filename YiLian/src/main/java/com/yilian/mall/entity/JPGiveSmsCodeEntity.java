package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2016/11/1 0001.
 */

public class JPGiveSmsCodeEntity extends BaseEntity {

    /**
     * token :
     * given_integral :"    //新用户注册时获赠的奖券数量，0表示没有获得赠送奖券
     * integral_balance : //用户当前积发奖励包
     */

    @SerializedName("token")
    public String token;
    @SerializedName("given_integral")
    public String givenIntegral;
    @SerializedName("integral_balance")
    public String integralBalance;
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2016/10/28 0028.
 */

public class JPTurnTableEntity extends BaseEntity{

    /**
     * sql : INSERT INTO `activity_daily` (`daily_activity`,`daily_user`,`user_gain_integral`,`user_before_integral`,`user_later_integral`,`user_gain_coupon`,`user_before_coupon`,`user_later_coupon`,`user_gain_lebi`,`user_before_lebi`,`user_later_lebi`,`coupon_expend`,`jion_time` ) VALUES ('1923','4026461104525111',0,0,0,'500','2001200','2001700',0,0,0,0,'1477646536');
     * result : 恭喜您,获得5抵扣券~
     * jion_count : 1
     * prize : 4
     */

    @SerializedName("result")
    public String result;
    @SerializedName("jion_count")
    public int jionCount;
    @SerializedName("prize")
    public int prize;
}

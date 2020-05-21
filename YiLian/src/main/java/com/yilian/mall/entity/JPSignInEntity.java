package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2016/10/28 0028.
 */

public class JPSignInEntity extends BaseEntity{

    /**
     * lucky_mponey : 400
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("luck_money")
        public int luckMoney;
    }
}

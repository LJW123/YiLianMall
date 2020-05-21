package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/8/15 0015.
 */

public class MerchantPraiseEntity extends BaseEntity {

    /**
     * is_praise : 1
     * praise_count : 2
     */

    @SerializedName("is_praise")
    public int isPraise;
    @SerializedName("praise_count")
    public int praiseCount;
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yukun on 2016/3/30.
 */
public class DailyId extends BaseEntity {

    @SerializedName("daily_id")
    public String dailyId;

    @SerializedName("jion_count")
    public int joinCount;


}

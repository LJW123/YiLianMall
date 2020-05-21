package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yukun on 2016/3/31.
 */
public class DailyTurntable extends BaseEntity {

    public String result; //中奖结果

    @SerializedName("jion_count")
    public int joinCount;

    public int prize;
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2017/7/10 0010.
 */

public class ScoreExponent implements Serializable{
    /**
     * integralNumber : 0.493600% //指数
     * created_at : 1499648714 //时间
     */

    @SerializedName("integralNumber")
    public String integralNumber;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("integralNumbers")
    public String integralNumbers;//不带万分比的指数字段
}

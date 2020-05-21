package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by zhaiyaohua on 2018/5/22.
 * sn通用跳转Model
 */

public class SnJumpEntity {
    /**
     * 显示文字
     */
    @SerializedName("title")
    public String title;
    /**
     * 图标/图片
     */
    @SerializedName("img")
    public String img;
    /**
     * 跳转类型
     */
    @SerializedName("type")
    public int type;
    /**
     * 跳转内容
     */
    @SerializedName("content")
    public String content;
}

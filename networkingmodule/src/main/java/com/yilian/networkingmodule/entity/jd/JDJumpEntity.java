package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by  on 2018/5/22.
 * JD 通用跳转Model
 */

public class JDJumpEntity {
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

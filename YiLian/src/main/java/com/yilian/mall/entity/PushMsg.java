package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/9.
 */
public class PushMsg implements Serializable {
    public String type;

    /**
     * 推送消息主键，数据格式为 ["type","id"],//type类型，id主键,目前只有活动页面type，其他的只用到id
     */
    public List<String> id;
    /**
     * 推送消息的标题
     */
    public String title;
    /**
     * 推送消息的内容
     */
    public String message;
    /**
     * 推送消息显示的图标
     */
    public String icon;
    /**
     * 用于统计该条推送是否被用户点击
     */
    public String pushId;
    /**
     * 目前只用于语音播报内容
     */
    public String content;
    /**
     * 0通知 1透传
     */
    @SerializedName("push_type")
    public int pushType;
}

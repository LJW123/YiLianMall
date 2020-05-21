package com.yilian.mylibrary.entity;/**
 * Created by  on 2017/3/23 0023.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2017/3/23 0023.
 * 乐分快报
 */
public class NoticeViewEntity implements Serializable {
    @SerializedName("type")
    public int type;
    @SerializedName("content")
    public String content;
    @SerializedName("name")
    public String msg;

//_________________________拼团列表的公告entity_____________________________________

    @SerializedName("user_name")
    public String userName;
    @SerializedName("goods_name")
    public String goodsName;
    @SerializedName("vouchar_time")
    public String voucharTime;


    @Override
    public String toString() {
        return "NoticeViewEntity{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", msg='" + msg + '\'' +
                ", userName='" + userName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", voucharTime='" + voucharTime + '\'' +
                '}';
    }
}

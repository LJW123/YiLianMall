package com.yilian.mall.entity;/**
 * Created by  on 2017/3/23 0023.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/3/23 0023.
 * 乐分快报
 */
public class NoticeViewEntity extends com.yilian.networkingmodule.entity.BaseEntity{
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
}

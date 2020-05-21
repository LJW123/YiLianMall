package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/15.
 */
public class CashURLEntity extends BaseEntity {

    @SerializedName("url")//分享链接 自行把转移符去掉
    public String url;
    @SerializedName("count")//现在会员人数
    public int count;
}

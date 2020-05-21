package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2016/12/3 0003.
 * V3版本的分享实体类
 */

public class JPShareEntity extends BaseEntity{

    /**
     * title : 阿迪达斯官方旗舰店
     * sub_title : LFMaLL
     * content : 阿迪达斯（adidas）是德国运动用品制造商，阿迪达斯AG的成员公司。以其创办人阿道夫·阿迪·达斯勒（Adolf Adi Dassler）命名，在1920年于接近纽伦堡的黑措根奥拉赫开始生产鞋类产品。1949年8月18日以adidas AG名字登记
     * img_url : http://img.lefenmall.net/goods/goods_info/82fe6c184d0a506f01c38a730486553651b260cc_icon.png
     * url : http://test.lefenmall.com/wechat/m/appShare.php?sign=0
     */

    @SerializedName("title")
    public String title;
    @SerializedName("sub_title")
    public String subTitle;
    @SerializedName("content")
    public String content;
    @SerializedName("img_url")
    public String imgUrl;
    @SerializedName("url")
    public String url;
}

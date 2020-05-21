package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 从网络加载幸运购分区数据实体类
 * Created by Administrator on 2016/7/27.
 */
public class OneBuyEntity extends BaseEntity {

    @SerializedName("data")
    public Data data;

    public class Data{
        @SerializedName("icon_class")//分类图标
        public ArrayList<iconClass> iconClasses;
        @SerializedName("banner")
        public ArrayList<banner> banners;
    }
    //分类图标
    public class iconClass{
        @SerializedName("type")// 0 跳转默认 1 URL
        public String type;
        @SerializedName("content")//内容
        public String content;
        @SerializedName("image_url")//图片
        public String image_url;
        @SerializedName("title")//分类名称
        public String title;
    }
    //轮播图
    public class banner{
        @SerializedName("banner_type")// 0 URL 1 活动
        public int type;
        @SerializedName("banner_data")//内容
        public String banner_data;
        @SerializedName("image_url")//图片
        public String image_url;
        @SerializedName("titles")
        public String titles;
    }
}

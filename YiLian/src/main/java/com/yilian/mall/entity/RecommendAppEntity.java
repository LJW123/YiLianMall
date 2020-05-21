package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ASUS on 2016/9/8 0008.
 */
public class RecommendAppEntity extends BaseEntity {


    /**
     * title :
     * msg :
     * icon :
     * url :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        @SerializedName("title")
        public String title;
        @SerializedName("msg")
        public String msg;
        @SerializedName("icon")
        public String icon;
        @SerializedName("url")
        public String url;

        @SerializedName("size")
        public float size;
        @SerializedName("count")
        public int count;
    }
}

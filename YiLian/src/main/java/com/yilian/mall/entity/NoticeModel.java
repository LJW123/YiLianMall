package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/7/13 0013.
 */

public class NoticeModel extends BaseEntity {

    @SerializedName("news")
    public ArrayList<NewsBean> news;

    public static class NewsBean {
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public int type;
        @SerializedName("image")
        public String image;
        @SerializedName("content") //链接
        public String content;
        @SerializedName("pv") //浏览量
        public String pv;
        @SerializedName("recomend") //首推标记
        public String recomend;
        @SerializedName("intro")
        public String intro;//简介
        @SerializedName("time")
        public String time;

    }
}

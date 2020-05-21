package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/2/12.
 */

public class LeDouHomePageDataEntity extends HttpResultBean {

    @SerializedName("banner")
    public List<BannerBean> banner;
    @SerializedName("icon")
    public List<IconBean> icon;
    @SerializedName("data")
    public List<DataBean> data;

    public static class BannerBean {
        /**
         * img : /app/images/icon/20171206/pay.png
         * type : 18
         * content : www.baidu.com
         */

        @SerializedName("img")
        public String img;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
    }

    public static class IconBean {
        /**
         * img : /app/icon/20161027/nvzhuang.png
         * name : 女装
         * type : 18
         * content : 1
         */

        @SerializedName("img")
        public String img;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
    }
}

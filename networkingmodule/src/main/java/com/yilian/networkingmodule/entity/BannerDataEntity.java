package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/12/12.
 */

public class BannerDataEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 1
         * title : 幸运购活动页面
         * image :
         * type : 9
         * content :
         */

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String title;
        @SerializedName("image")
        public String image;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
    }
}

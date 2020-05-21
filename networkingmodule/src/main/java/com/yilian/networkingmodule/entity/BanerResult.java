package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ZYH on 2017/12/9 0009.
 */

public class BanerResult extends HttpResultBean {
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * id : 1
         * title : 幸运购活动页面
         * image :
         * type : 9
         * content :
         */
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("image")
        public String image;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
    }
}

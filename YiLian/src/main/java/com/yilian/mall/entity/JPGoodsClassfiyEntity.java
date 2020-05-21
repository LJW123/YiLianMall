package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2016/10/19 0019.
 */

public class JPGoodsClassfiyEntity extends BaseEntity {


    /**
     * parent : 0
     * id : 1
     * img : nvzhuang.png
     * name : 女装
     * info : [{"parent":"0","id":"1","img":"nvzhuang.png","name":"女装"},{}]
     */

    @SerializedName("list")
    public List<ListBean> list;

    public class ListBean {
        @SerializedName("parent")
        public String parent;
        @SerializedName("id")
        public String id;
        @SerializedName("img")
        public String img;
        @SerializedName("name")
        public String name;
        /**
         * parent : 0
         * id : 1
         * img : nvzhuang.png
         * name : 女装
         */

        @SerializedName("info")
        public List<InfoBean> info;

        public class InfoBean {
            @SerializedName("parent")
            public String parent;
            @SerializedName("id")
            public String id;
            @SerializedName("img")
            public String img;
            @SerializedName("name")
            public String name;
        }
    }
}

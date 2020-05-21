package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/8/23 0023.
 */

public class ClassifyListV2Entity extends HttpResultBean {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean implements Serializable {
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("level")
        public String level;
        @SerializedName("parent")
        public String parent;
        @SerializedName("img")
        public String img;
        @SerializedName("addtime")
        public String addtime;
        @SerializedName("sort")
        public String sort;
        @SerializedName("is_show")
        public String is_show;
        @SerializedName("is_integral")
        public String is_integral;
        @SerializedName("is_gou")
        public String is_gou;
        @SerializedName("is_del")
        public String is_del;

        @Override
        public String toString() {
            return "ListBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", level='" + level + '\'' +
                    ", parent='" + parent + '\'' +
                    ", img='" + img + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", sort='" + sort + '\'' +
                    ", is_show='" + is_show + '\'' +
                    ", is_integral='" + is_integral + '\'' +
                    ", is_gou='" + is_gou + '\'' +
                    ", is_del='" + is_del + '\'' +
                    '}';
        }
//        @SerializedName("info")
//        public ArrayList<InfoBean> info;
//
//        public class InfoBean implements Serializable {
//            @SerializedName("id")
//            public String id;
//            @SerializedName("name")
//            public String name;
//            @SerializedName("level")
//            public String level;
//            @SerializedName("parent")
//            public String parent;
//            @SerializedName("img")
//            public String img;
//            @SerializedName("addtime")
//            public String addtime;
//            @SerializedName("sort")
//            public String sort;
//            @SerializedName("is_show")
//            public String is_show;
//            @SerializedName("is_integral")
//            public String is_integral;
//            @SerializedName("is_gou")
//            public String is_gou;
//        }
    }
}

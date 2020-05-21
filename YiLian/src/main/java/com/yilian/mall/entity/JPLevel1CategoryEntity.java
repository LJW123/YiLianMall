package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2016/10/19 0019.
 */

public class JPLevel1CategoryEntity extends BaseEntity {

    /**
     * parent : 0
     * id : 1
     * img : nvzhuang.png
     * name : 女装
     */

    @SerializedName("list")
    public ArrayList<ListBean> JPLevel1CategoryList;

    /**
     * 新版商城 一级分类实体类
     */
    public static class ListBean implements Serializable {
        @SerializedName("parent")
        public String JPLevel1CategoryParent;
        @SerializedName("id")
        public String JPLevel1CategoryId;
        @SerializedName("img")
        public String JPLevel1CategoryImg;
        @SerializedName("name")
        public String JPLevel1CategoryName;

        @Override
        public String toString() {
            return "ListBean{" +
                    "JPLevel1CategoryParent='" + JPLevel1CategoryParent + '\'' +
                    ", JPLevel1CategoryId='" + JPLevel1CategoryId + '\'' +
                    ", JPLevel1CategoryImg='" + JPLevel1CategoryImg + '\'' +
                    ", JPLevel1CategoryName='" + JPLevel1CategoryName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "JPLevel1CategoryEntity{" +
                "JPLevel1CategoryList=" + JPLevel1CategoryList +
                '}';
    }
}

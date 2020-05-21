package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by  on 2016/10/20 0020.
 */

public class CategoryHeaderEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean JPCategoryData;

    public class DataBean implements Serializable {
        /**
         * banner_type : 1
         * banner_data :
         * image_url :
         * titles :
         */

        @SerializedName("app_banner")
        public ArrayList<BannerEntity> JPCategoryAppBanner;
        /**
         * parent : 0
         * id : 1
         * img : nvzhuang.png
         * name : 女装
         */

        @SerializedName("icon_class")
        public ArrayList<CategoryIconClassBean> JPCategoryIconClass;


        public class CategoryIconClassBean implements Serializable {
            @SerializedName("parent")
            public String JPCategoryParent;
            @SerializedName("id")
            public String JPCategoryId;
            @SerializedName("img")
            public String JPCategoryImg;
            @SerializedName("name")
            public String JPCategoryName;

            @Override
            public String toString() {
                return "CategoryIconClassBean{" +
                        "JPCategoryParent='" + JPCategoryParent + '\'' +
                        ", JPCategoryId='" + JPCategoryId + '\'' +
                        ", JPCategoryImg='" + JPCategoryImg + '\'' +
                        ", JPCategoryName='" + JPCategoryName + '\'' +
                        '}';
            }
        }
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/19 0019.
 */

public class LeFenHome extends BaseEntity{

    /**
     * filiale_id : 2
     * filiale_graded : 20
     * filiale_image : []
     * filiale_name : 益联益家总部
     * filiale_province : 11
     * filiale_city : 149
     * filiale_county : 0
     * filiale_address :
     * filiale_longitude : 113.835305
     * filiale_latitude : 34.804388
     * filiale_tel : 15010189500
     * filiale_desp : 益联益家总部
     * shop_worktime : 08:40--18:10
     * shop_list : [{"shop_index":"","shop_name":"","shop_address":"","shop_image":[]},{}]
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("filiale_id")
        public String filialeId;
        @SerializedName("filiale_graded")
        public float filialeGraded;
        @SerializedName("filiale_name")
        public String filialeName;
        @SerializedName("filiale_province")
        public String filialeProvince;
        @SerializedName("filiale_city")
        public String filialeCity;
        @SerializedName("filiale_county")
        public String filialeCounty;
        @SerializedName("filiale_address")
        public String filialeAddress;
        @SerializedName("filiale_longitude")
        public String filialeLongitude;
        @SerializedName("filiale_latitude")
        public String filialeLatitude;
        @SerializedName("filiale_tel")
        public String filialeTel;
        @SerializedName("filiale_desp")
        public String filialeDesp;
        @SerializedName("shop_worktime")
        public String shopWorktime;

        /**
         * photo_name :
         * photo_path :
         */
        @SerializedName("filiale_image")
        public List<ImageListBean> imageList;

        public static class ImageListBean {
            @SerializedName("photo_name")
            public String photoName;
            @SerializedName("photo_path")
            public String photoPath;
        }

        /**
         * shop_index :
         * shop_name :
         * shop_address :
         * shop_image : []
         */

        @SerializedName("shop")
        public ArrayList<ShopListBean> shop;

        public class ShopListBean {
            @SerializedName("shop_index")
            public String shopIndex;
            @SerializedName("shop_name")
            public String shopName;
            @SerializedName("shop_address")
            public String shopAddress;
            @SerializedName("shop_image")
            public String shopImage;
        }
    }
}

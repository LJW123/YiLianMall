package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2016/10/21 0021.
 * 本地界面 头部 实体类
 */

public class JPLocalHeaderEntity extends BaseEntity {

    /**
     * filiale_id :
     * app_banner : [{"banner_type":1,"banner_data":"","image_url":"","titles":""},{}]
     * icon_class : [{"type":1,"content":"","image_url":"","name":""},{}]
     * activity : [{"activity":1,"msg":"","image_url":""},{}]
     * goods : [{"goods_name":"","goods_id":"","supplier_id":"","filiale_id":"","price":"","cost":"","image_url":""},{}]
     */

    @SerializedName("data")
    public DataBean JPLocalHeaderData;

    public class DataBean {
        @SerializedName("filiale_id")
        public String JPLocalHeaderFilialeId;
        /**
         * banner_type : 1
         * banner_data :
         * image_url :
         * titles :
         */

        /**
         * 兑换中心数量
         */
        @SerializedName("filiale_count")
        public String JPFilialeCount;


        @SerializedName("app_banner")
        public ArrayList<JPBannerEntity> JPLocalHeaderAppBanner;
        /**
         * type : 1
         * content :
         * image_url :
         * name :
         */

        @SerializedName("icon_class")
        public ArrayList<IconClassBean> JPLocalHeaderIconClass;
        /**
         * activity : 1
         * msg :
         * image_url :
         */

        @SerializedName("activity")
        public ArrayList<ActivityBean> JPLocalHeaderActivity;
        @SerializedName("goods")
        public ArrayList<JPGoodsEntity> JPLocalGoods;

        public class ActivityBean {
            @SerializedName("activity")
            public int JPLocalHeaderActivity;
            @SerializedName("content")
            public String JPLocalHeaderContent;
            @SerializedName("msg")
            public String JPLocalHeaderMsg;
            @SerializedName("image_url")
            public String JPLocalHeaderImageUrl;
        }
        @SerializedName("merchant_package")
        public ArrayList<JPLocalMTMerchantPackageEntity> jpLocalMTMerchantPackageEntities;

    }
}

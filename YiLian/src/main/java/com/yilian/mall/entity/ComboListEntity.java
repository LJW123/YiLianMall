package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.entity.LocationEntity;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/7 0007.
 */

public class ComboListEntity extends BaseEntity {

    /**
     * merchant_id :
     * merchant_name :
     * merchant_desp :
     * merchant_longitude :
     * merchant_latitude :
     * merchant_industry :
     * merchant_industry_parent :
     * merchant_scope :
     * merchant_address :
     * merchant_image :
     * industry_name :
     * praise_count :
     * sell_type :
     * evaluate :
     * packages : [{"package_id":"","package_icon":"","name":"","price":"","sell_count":"","status":"","scope":""},{}]
     */

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean extends LocationEntity {

        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_desp")
        public String merchantDesp;
        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_scope")
        public String merchantScope;
        @SerializedName("merchant_address")
        public String merchantAddress;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("is_delivery")
        public String isDelivery;
        @SerializedName("evaluate")
        public String evaluate;
        /**
         * package_id :
         * package_icon :
         * name :
         * price :
         * sell_count :
         * status :
         * scope :
         */

        @SerializedName("packages")
        public List<PackagesBean> packages;

        public static class PackagesBean {
            @SerializedName("package_id")
            public String packageId;
            @SerializedName("package_icon")
            public String packageIcon;
            @SerializedName("name")
            public String name;
            @SerializedName("price")
            public String price;
            @SerializedName("sell_count")
            public String sellCount;
            @SerializedName("status")
            public String status;
            @SerializedName("scope")
            public String scope;


        }
    }
}

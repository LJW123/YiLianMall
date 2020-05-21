package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/14 0014.
 */
public class MTComboSearchEntity extends BaseEntity{

    /**
     * merchant_id :
     * merchant_name :
     * merchant_longitude :
     * merchant_latitude :
     * merchant_industry :
     * merchant_industry_parent :
     * merchant_image :
     * industry_name :
     * praise_count :
     * is_delivery :
     * evaluate :
     * packages : [{"package_id":"","package_icon":"","name":"","price":"","sell_count":""},{}]
     */

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_longitude")
        public String merchantLongitude;
        @SerializedName("merchant_latitude")
        public String merchantLatitude;
        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("is_delivery")
        public String isDelivery;//0 不支持外卖1支持外卖
        @SerializedName("evaluate")
        public String evaluate;
        @SerializedName("merchant_address")
        public String merchantAddress;
        public String distance;
//        //本地商家列表的字段
//        @SerializedName("sell_type")
//        public String sellType;
        /**
         * package_id :
         * package_icon :
         * name :
         * price :
         * sell_count :
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
        }
    }
}

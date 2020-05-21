package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/6/16 0016.
 */

public class MerchantDiscountEntity extends BaseEntity {
    /**
     * data : {"merchant_id":"77","merchant_name":"Djdjjfif","merchant_percent":8.5,"merchant_image":"app/images/mall/20170616/6178629170191004_426140_1497599335979environment.jpg"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * merchant_id : 77
         * merchant_name : Djdjjfif
         * merchant_percent : 8.5
         * merchant_image : app/images/mall/20170616/6178629170191004_426140_1497599335979environment.jpg
         * merchant_bonus 商家获取益豆百分比例
         */

        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_percent")
        public float merchantPercent;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("merchant_bonus")
        public float merchantBonus;
    }
//
//    /**
//     * data : {"merchant_percent":"","merchant_image":"","merchant_name":""}
//     */
//
//    @SerializedName("data")
//    public DataBean data;
//
//    public static class DataBean {
//        /**
//         * merchant_percent :
//         * merchant_image :
//         * merchant_name :
//         */
//
//        @SerializedName("merchant_percent")
//        public int merchantPercent;
//        @SerializedName("merchant_image")
//        public String merchantImage;
//        @SerializedName("merchant_name")
//        public String merchantName;
//    }

}

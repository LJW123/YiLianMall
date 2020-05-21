package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/8 0008.
 * 套餐详情
 */
public class  MTComboDetailsEntity extends  BaseEntity {

    /**
     * login_status :
     * yet_collect :
     * data : {"package_id":"","package_name":"","package_icon":[{"path":""},{}],"price":"","sell_count":"","evaluate":"","full_minus_fee":"","merchant_info":{"merchant_id":"","merchant_name":"","merchant_desp":"","merchant_longitude":"","merchant_latitude":"","merchant_address":"","merchant_contact":""},"tags":[{"tags_name":"","tags_count":""},{}],"package_info":[{"package_name":"","content":[{"name":"","number":"","cost":""},{}]},{}],"notes_info":{"use_rules":"","usable_time":"","effective_time":""},"evaluate_count":"","evaluate_list":[{"name":"","image_url":"","evaluate":"","commit_date":"","taste":"","environment":"","service":"","consumer_comment":"","merchant_comment":"","album":""}]}
     */

    @SerializedName("login_status")
    public String loginStatus;
    @SerializedName("yet_collect")
    public String yetCollect;
    /**
     * package_id :
     * package_name :
     * package_icon : [{"path":""},{}]
     * price :
     * sell_count :
     * evaluate :
     * full_minus_fee :
     * merchant_info : {"merchant_id":"","merchant_name":"","merchant_desp":"","merchant_longitude":"","merchant_latitude":"","merchant_address":"","merchant_contact":""}
     * tags : [{"tags_name":"","tags_count":""},{}]
     * package_info : [{"package_name":"","content":[{"name":"","number":"","cost":""},{}]},{}]
     * notes_info : {"use_rules":"","usable_time":"","effective_time":""}
     * evaluate_count :
     * evaluate_list : [{"name":"","image_url":"","evaluate":"","commit_date":"","taste":"","environment":"","service":"","consumer_comment":"","merchant_comment":"","album":""}]
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("package_id")
        public String packageId;
        @SerializedName("package_name")
        public String packageName;
        @SerializedName("price")
        public String price;
        @SerializedName("voucher")
        public int voucher;
        @SerializedName("is_profit")
        public int isProfit;
        @SerializedName("sell_count")
        public String sellCount;
        @SerializedName("evaluate")
        public String evaluate;
        @SerializedName("full_minus_fee")
        public String fullMinusFee;
        @SerializedName("scope")
        public  String scope;
        @SerializedName("is_delivery")
        public String isDelivery;
        @SerializedName("delivery_amount")
        public String deliveryAmount;
        @SerializedName("price_total")
        public String priceTotal;

        /**
         * merchant_id :
         * merchant_name :
         * merchant_desp :
         * merchant_longitude :
         * merchant_latitude :
         * merchant_address :
         * merchant_contact :
         */

        @SerializedName("merchant_info")
        public MerchantInfoBean merchantInfo;
        /**
         * use_rules :
         * usable_time :
         * effective_time :
         */

        @SerializedName("notes_info")
        public NotesInfoBean notesInfo;
        @SerializedName("evaluate_count")
        public String evaluateCount;
        /**
         * path :
         */

        @SerializedName("package_icon")
        public List<PackageIconBean> packageIcon;
        /**
         * tags_name :
         * tags_count :
         */

        @SerializedName("tags")
        public List<TagsBean> tags;
        /**
         * package_name :
         * content : [{"name":"","number":"","cost":""},{}]
         */

        @SerializedName("package_info")
        public List<PackageInfoBean> packageInfo;
        /**
         * name :
         * image_url :
         * evaluate :
         * commit_date :
         * taste :
         * environment :
         * service :
         * consumer_comment :
         * merchant_comment :
         * album :
         */

        @SerializedName("evaluate_list")
        public List<EvaluateListBean> evaluateList;

        public class MerchantInfoBean {
            @SerializedName("merchant_id")
            public String merchantId;
            @SerializedName("merchant_name")
            public String merchantName;
            @SerializedName("merchant_desp")
            public String merchantDesp;
            @SerializedName("merchant_longitude")
            public String merchantLongitude;
            @SerializedName("merchant_latitude")
            public String merchantLatitude;
            @SerializedName("merchant_address")
            public String merchantAddress;
            @SerializedName("merchant_contact")
            public String merchantContact;
        }

        public class NotesInfoBean {
            @SerializedName("use_rules")
            public String useRules;
            @SerializedName("usable_time")
            public String usableTime;
            @SerializedName("effective_time")
            public String effectiveTime;

        }

        public class PackageIconBean {
            @SerializedName("path")
            public String path;
        }

        public class TagsBean {
            @SerializedName("tags_name")
            public String tagsName;
            @SerializedName("tags_count")
            public String tagsCount;

            @Override
            public String toString() {
                return "TagsBean{" +
                        "tagsName='" + tagsName + '\'' +
                        ", tagsCount='" + tagsCount + '\'' +
                        '}';
            }
        }

        public class PackageInfoBean {
            @SerializedName("package_name")
            public String packageName;
            /**
             * name :
             * number :
             * cost :
             */

            @SerializedName("content")
            public List<ContentBean> content;

            public class ContentBean {
                @SerializedName("name")
                public String name;
                @SerializedName("number")
                public String number;
                @SerializedName("cost")
                public String cost;
            }
        }

        public  class EvaluateListBean {
            @SerializedName("name")
            public String name;
            @SerializedName("image_url")
            public String imageUrl;
            @SerializedName("evaluate")
            public String evaluate;
            @SerializedName("commit_date")
            public String commitDate;
            @SerializedName("taste")
            public String taste;
            @SerializedName("environment")
            public String environment;
            @SerializedName("service")
            public String service;
            @SerializedName("consumer_comment")
            public String consumerComment;
            @SerializedName("merchant_comment")
            public String merchantComment;
            @SerializedName("album")
            public String album;
        }
    }
}

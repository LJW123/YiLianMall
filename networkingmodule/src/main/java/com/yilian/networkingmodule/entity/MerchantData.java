package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2017/6/15 0015.
 * 商家资料
 */

public class MerchantData extends BaseEntity {

    /**
     * data : {"merchant_id":"14","consumer_id":"6103273871108110","merchant_name":"建设银行","maker_userid":"0","merchant_tel":"123456789","merchant_contacts":"就是你","merchant_province":"11","merchant_city":"149","merchant_county":"1251","merchant_address":"老人头！建立起点就业直通车优化设计","merchant_industry_parent":"38","merchant_industry":"38","merchant_desp":"这是简介","merchant_image":"app/images/card/20170615/6103273871108110_1594192_image.jpg","merchant_worktime":"09:37,21:37","merchant_refuse":"","merchant_percent":"84","register_time":"0","praise_count":"0","renqi":"0","info_version":"23","merchant_longitude":"91.26154431508002","merchant_latitude":"29.68326325148983","audit_time":"0","person_id":"0","check_status":"1","merchant_sort_time":"0","merchant_type":"1","industry_parent_name":"洗衣、干洗","merchant_industry_name":"洗衣、干洗","open_time":"09:37","close_time":"21:37","merchant_album":["app/images/card/20170615/6103273871108110_1712339_image.jpg","app/images/card/20170615/6103273871108110_7497870_image.jpg","app/images/card/20170615/6103273871108110_5345359_image.jpg"]}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * merchant_id : 14
         * consumer_id : 6103273871108110
         * merchant_name : 建设银行
         * maker_userid : 0
         * merchant_tel : 123456789
         * merchant_contacts : 就是你
         * merchant_province : 11
         * merchant_city : 149
         * merchant_county : 1251
         * merchant_address : 老人头！建立起点就业直通车优化设计
         * merchant_industry_parent : 38
         * merchant_industry : 38
         * merchant_desp : 这是简介
         * merchant_image : app/images/card/20170615/6103273871108110_1594192_image.jpg
         * merchant_worktime : 09:37,21:37
         * merchant_refuse :
         * merchant_percent : 84
         * register_time : 0
         * praise_count : 0
         * renqi : 0
         * info_version : 23
         * merchant_longitude : 91.26154431508002
         * merchant_latitude : 29.68326325148983
         * audit_time : 0
         * person_id : 0
         * check_status : 1
         * merchant_sort_time : 0
         * merchant_type : 1
         * industry_parent_name : 洗衣、干洗
         * merchant_industry_name : 洗衣、干洗
         * open_time : 09:37
         * close_time : 21:37
         * merchant_album : ["app/images/card/20170615/6103273871108110_1712339_image.jpg","app/images/card/20170615/6103273871108110_7497870_image.jpg","app/images/card/20170615/6103273871108110_5345359_image.jpg"]
         */

        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("consumer_id")
        public String consumerId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("maker_userid")
        public String makerUserid;
        @SerializedName("merchant_tel")
        public String merchantTel;
        @SerializedName("merchant_contacts")
        public String merchantContacts;
        @SerializedName("merchant_province")
        public String merchantProvince;
        @SerializedName("merchant_city")
        public String merchantCity;
        @SerializedName("merchant_county")
        public String merchantCounty;
        @SerializedName("merchant_address")
        public String merchantAddress;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_desp")
        public String merchantDesp;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("merchant_worktime")
        public String merchantWorktime;
        @SerializedName("merchant_refuse")
        public String merchantRefuse;
        @SerializedName("merchant_percent")
        public String merchantPercent;
        @SerializedName("register_time")
        public String registerTime;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("renqi")
        public String renqi;
        @SerializedName("info_version")
        public String infoVersion;
        @SerializedName("merchant_longitude")
        public String merchantLongitude;
        @SerializedName("merchant_latitude")
        public String merchantLatitude;
        @SerializedName("audit_time")
        public String auditTime;
        @SerializedName("person_id")
        public String personId;
        @SerializedName("check_status")
        public String checkStatus;
        @SerializedName("merchant_sort_time")
        public String merchantSortTime;
        @SerializedName("merchant_type")
        public String merchantType;
        @SerializedName("industry_parent_name")
        public String industryParentName;
        @SerializedName("merchant_industry_name")
        public String merchantIndustryName;
        @SerializedName("open_time")
        public String openTime;
        @SerializedName("close_time")
        public String closeTime;
        @SerializedName("keywords")
        //关键词
        public String keyWords;
        @SerializedName("merchant_album")
        public ArrayList<String> merchantAlbum;
    }
}

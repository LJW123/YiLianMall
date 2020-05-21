package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.entity.LocationEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 *
 * @author Ray_L_Pain
 * @date 2017/12/21 0021
 */

public class V5MerchantListEntity extends HttpResultBean {

    @SerializedName("merchant_list")
    public ArrayList<MerchantListBean> merchantList;

    public class MerchantListBean extends LocationEntity {

        @SerializedName("merchant_sort_time")
        public long merchantSortTime;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("evaluate")
        public String evaluate;
        @SerializedName("merchant_address")
        public String merchantAddress;

        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_tel")
        public String merchantTel;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("mer_discount")
        public String merDiscount;//商家让利百分比

        /**
         * 浏览量
         */
        @SerializedName("renqi")
        public String renqi;

        /**
         * 是否支持配送
         */
        @SerializedName("is_delivery")
        public String isDelivery;
        /**
         * 是否支持预定
         */
        @SerializedName("is_reserve")
        public String isReserve;
    }
}

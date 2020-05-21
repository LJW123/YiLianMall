package com.yilian.mall.entity;/**
 * Created by  on 2017/3/23 0023.
 */

import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.entity.LocationEntity;

import java.util.List;

/**
 * Created by  on 2017/3/23 0023.
 */
public class ShopsEntity extends BaseEntity {

    @SerializedName("merchant_list")
    public List<MerchantListBean> merchantList;

    public static class MerchantListBean extends LocationEntity {
        /**
         * graded : 50
         * industry_name : 自助餐
         * is_delivery : 0
         * is_reserve : 0
         * merchant_address : 益联益家总部
         * merchant_city : 149
         * merchant_county : 1251
         * merchant_desp : 海鲜 适宜商务宴请、休闲时光、休闲小憩、随便吃吃、情侣约会、家庭聚会、朋友聚餐麦子天生性格豪爽，招了不少朋友的喜爱。
         * merchant_id : 1
         * merchant_image : merchant_image/20150920/20150920093359_160069jpg
         * merchant_industry : 14
         * merchant_industry_parent : 3
         * merchant_latitude : 34.755445
         * merchant_longitude : 113.731654
         * merchant_name : 乐分总部
         * merchant_province : 11
         * merchant_scope : 烤生蚝(2032 )帝王蟹(1876 )奶油酥皮汤(1799 )生鱼片(1766 )三文鱼(1739 )鲍鱼(1707 )哈根达斯(1671 )巧克力(1600 )鱼翅(334 )烤乳猪(155 )煎牛仔骨(90 )咖喱蟹(55 )佛跳墙(22 )
         * merchant_tel : 0371-60138999
         * merchant_worktime : 09:00-22:00
         * praise_count : 0
         * renqi : 291
         */

        @SerializedName("graded")
        public String graded;
        @SerializedName("industry_name")
        public String industryName;
        @SerializedName("is_delivery")
        public String isDelivery;
        @SerializedName("is_reserve")
        public String isReserve;
        @SerializedName("merchant_address")
        public String merchantAddress;
        @SerializedName("merchant_city")
        public String merchantCity;
        @SerializedName("merchant_county")
        public String merchantCounty;
        @SerializedName("merchant_desp")
        public String merchantDesp;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("merchant_industry")
        public String merchantIndustry;
        @SerializedName("merchant_industry_parent")
        public String merchantIndustryParent;
        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_province")
        public String merchantProvince;
        @SerializedName("merchant_scope")
        public String merchantScope;
        @SerializedName("merchant_tel")
        public String merchantTel;
        @SerializedName("merchant_worktime")
        public String merchantWorktime;
        @SerializedName("praise_count")
        public String praiseCount;
        @SerializedName("renqi")
        public String renqi;
        @SerializedName("mer_discount")
        public String merDiscount;//商家让利的百分比

        @SerializedName("merchant_sort_time")
        public long merchantSortTime;//推荐时间戳，大于0代表推荐商家 否则代表非推荐商家
        @Override
        public String toString() {
            return "MerchantListBean{" +
                    "graded='" + graded + '\'' +
                    ", industryName='" + industryName + '\'' +
                    ", isDelivery='" + isDelivery + '\'' +
                    ", isReserve=" + isReserve +
                    ", merchantAddress='" + merchantAddress + '\'' +
                    ", merchantCity='" + merchantCity + '\'' +
                    ", merchantCounty='" + merchantCounty + '\'' +
                    ", merchantDesp='" + merchantDesp + '\'' +
                    ", merchantId='" + merchantId + '\'' +
                    ", merchantImage='" + merchantImage + '\'' +
                    ", merchantIndustry='" + merchantIndustry + '\'' +
                    ", merchantIndustryParent='" + merchantIndustryParent + '\'' +
                    ", merchantLatitude='" + merchantLatitude + '\'' +
                    ", merchantLongitude='" + merchantLongitude + '\'' +
                    ", merchantName='" + merchantName + '\'' +
                    ", merchantProvince='" + merchantProvince + '\'' +
                    ", merchantScope='" + merchantScope + '\'' +
                    ", merchantTel='" + merchantTel + '\'' +
                    ", merchantWorktime='" + merchantWorktime + '\'' +
                    ", praiseCount='" + praiseCount + '\'' +
                    ", renqi='" + renqi + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShopsEntity{" +
                "merchantList=" + merchantList.toString() +
                '}';
    }
}

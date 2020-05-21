package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaofo on 2018/8/30.
 */

public class CtripHotelDetailEntity extends HttpResultBean {


    @SerializedName("data")
    public DataBean data;

    public static class DataBean {

        @SerializedName("id")
        public String id;
        @SerializedName("HotelID")
        public String hotelID;
        @SerializedName("HotelName")
        public String hotelName;
        @SerializedName("StarRating")
        public String starRating;
        @SerializedName("StarRatingDec")
        public String starRatingDec;
        @SerializedName("OpenYear")
        public String openYear;
        @SerializedName("RenovationYear")
        public String renovationYear;
        @SerializedName("RoomQuantity")
        public String roomQuantity;
        @SerializedName("Bookable")
        public String bookable;
        @SerializedName("CityCode")
        public String cityCode;
        @SerializedName("CityName")
        public String cityName;
        @SerializedName("AreaCode")
        public String areaCode;
        @SerializedName("AreaName")
        public String areaName;
        @SerializedName("Address")
        public String address;
        @SerializedName("AdjacentIntersection")
        public String adjacentIntersection;
        @SerializedName("BusinessDistrict")
        public String businessDistrict;
        @SerializedName("gdLat")
        public double gdLat;
        @SerializedName("gdLng")
        public double gdLng;
        @SerializedName("BrandCode")
        public String brandCode;
        @SerializedName("BrandName")
        public String brandName;
        @SerializedName("GroupName")
        public String groupName;
        @SerializedName("CtripRecommendLevel")
        public String ctripRecommendLevel;
        @SerializedName("CtripUserRating")
        public String ctripUserRating;
        @SerializedName("CtripUserRatingsDec")
        public String ctripUserRatingsDec;
        @SerializedName("CtripUserRatingDec")
        public String ctripUserRatingDec;
        @SerializedName("FacilitiesID")
        public String facilitiesID;
        @SerializedName("image")
        public String image;
        @SerializedName("Themes")
        public String themes;
        @SerializedName("ThemesName")
        public String themesName;
        @SerializedName("Telephone")
        public String telephone;
        @SerializedName("IsMustBe")
        public String isMustBe;
        @SerializedName("EarliestTime")
        public String earliestTime;
        @SerializedName("LatestTime")
        public String latestTime;
        @SerializedName("NormalizedPolicies")
        public NormalizedPoliciesBean normalizedPolicies;
        @SerializedName("Distance")
        public String distance;
        @SerializedName("MinPrice")
        public String minPrice;
        @SerializedName("MinPrice_MyCurrency")
        public String minPriceMyCurrency;
        @SerializedName("Currency_minPrice")
        public String currencyMinPrice;
        @SerializedName("MinPriceRoom")
        public String minPriceRoom;
        @SerializedName("returnBean")
        public String returnBean;
        @SerializedName("BedIDs")
        public String bedIDs;
        @SerializedName("comSort")
        public String comSort;
        @SerializedName("ArrivalLatestTime")
        public String arrivalLatestTime;
        @SerializedName("Policies")
        public ArrayList<PoliciesBean> policies;
        @SerializedName("ImportantNotices")
        public List<ImportantNoticesBean> importantNotices;
        @SerializedName("Facilities")
        public ArrayList<FacilitiesBean> facilities;
        @SerializedName("Pictures")
        public List<PicturesBean> pictures;
        @SerializedName("Descriptions")
        public ArrayList<DescriptionsBean> descriptions;
        @SerializedName("TransportationInfos")
        public List<TransportationInfosBean> transportationInfos;
        @SerializedName("arrivalOperations")
        public ArrayList<ArrivalOperationsBean> arrivalOperations;
        @SerializedName("RoomTypeInfos")
        public List<CtripRoomTypeInfo> roomTypeInfos;

        public static class NormalizedPoliciesBean {
            /**
             * ChildAndExtraBedPolicy : {"Descriptions":[],"AllowChildrenToStay":false,"AllowUseExistingBed":false,"AllowExtraBed":false,"AllowExtraCrib":false}
             * MealsPolicy : []
             */

            @SerializedName("ChildAndExtraBedPolicy")
            public ChildAndExtraBedPolicyBean childAndExtraBedPolicy;
            @SerializedName("MealsPolicy")
            public List<?> mealsPolicy;

            public static class ChildAndExtraBedPolicyBean {
                /**
                 * Descriptions : []
                 * AllowChildrenToStay : false
                 * AllowUseExistingBed : false
                 * AllowExtraBed : false
                 * AllowExtraCrib : false
                 */

                @SerializedName("AllowChildrenToStay")
                public boolean allowChildrenToStay;
                @SerializedName("AllowUseExistingBed")
                public boolean allowUseExistingBed;
                @SerializedName("AllowExtraBed")
                public boolean allowExtraBed;
                @SerializedName("AllowExtraCrib")
                public boolean allowExtraCrib;
                @SerializedName("Descriptions")
                public List<?> descriptions;
            }
        }

        public static class PoliciesBean {
            /**
             * Text : 入住时间：14:00-22:00      离店时间：12:00以前
             * Code : CheckInCheckOut
             */

            @SerializedName("Text")
            public String text;
            @SerializedName("Code")
            public String code;
        }

        public static class ImportantNoticesBean {
            /**
             * Text : 目前北京全城禁烟，酒店均为无烟房。123
             * Category : City
             */

            @SerializedName("Text")
            public String text;
            @SerializedName("Category")
            public String category;
        }

        public static class FacilitiesBean {
            /**
             * FacilityItem : [{"ID":"1","Name":"中餐厅","Status":"1"}]
             * CategoryName : 通用设施
             */

            @SerializedName("CategoryName")
            public String categoryName;
            @SerializedName("FacilityItem")
            public List<FacilityItemBean> facilityItem;

            public static class FacilityItemBean {
                /**
                 * ID : 1
                 * Name : 中餐厅
                 * Status : 1
                 */

                @SerializedName("ID")
                public int id;
                @SerializedName("Name")
                public String name;
                @SerializedName("Status")
                public String status;
            }
        }

        public static class PicturesBean {
            /**
             * ExtraAttribute : {"Resolution":"","Rank":"1","Sharpness":"15","AestheticScore":"29.463","Source":"Ctrip"}
             * ID : 29809393
             * Type : 1
             * Caption : 外观
             * URL : https://dimg12.c-ctrip.com/images/t1/hotel/1000/215/55653d422ebe47869a63c4144ccccd4a_R_550_412.jpg
             */

            @SerializedName("ExtraAttribute")
            public ExtraAttributeBean extraAttribute;
            @SerializedName("ID")
            public String id;
            @SerializedName("Type")
            public String type;
            @SerializedName("Caption")
            public String caption;
            @SerializedName("URL")
            public String url;

            public static class ExtraAttributeBean {
                /**
                 * Resolution :
                 * Rank : 1
                 * Sharpness : 15
                 * AestheticScore : 29.463
                 * Source : Ctrip
                 */

                @SerializedName("Resolution")
                public String resolution;
                @SerializedName("Rank")
                public String rank;
                @SerializedName("Sharpness")
                public String sharpness;
                @SerializedName("AestheticScore")
                public String aestheticScore;
                @SerializedName("Source")
                public String source;
            }
        }

        public static class DescriptionsBean implements Serializable {
            /**
             * Text : 北京金海湖大唐辉煌传媒影视基地宾馆隶属于大唐辉煌传媒有限公司，位于风景秀丽的北京市平谷金海湖旅游风景区，宾馆距离首都机场仅半个小时的车程，宾馆拥有9栋别墅，涵盖客房、餐厅、贵宾室、书画院、KTV、舞厅、酒吧、茶室、会议室、棋牌室、平谷唯一一家3D电影院等功能，可同时容纳200人入住，除了作为拍摄场地外也是会展会议、休闲娱乐、旅游观光的绝佳去处。
             * Category : 1
             */

            @SerializedName("Text")
            public String text;
            @SerializedName("Category")
            public String category;
        }

        public static class TransportationInfosBean {
            /**
             * Coordinates : [{"Provider":"Unknown","LNG":117.1213760376,"LAT":40.140697479248},{"Provider":"Baidu","LNG":117.12775421143,"LAT":40.147048950195}]
             * Name : 平谷城区
             * Type : 1
             * Distance : 17.441
             * Directions : 驾车约17.441公里(约20分钟)
             * TransportationType : 0
             * TimeTaken : 20
             */

            @SerializedName("Name")
            public String name;
            @SerializedName("Type")
            public String type;
            @SerializedName("Distance")
            public String distance;
            @SerializedName("Directions")
            public String directions;
            @SerializedName("TransportationType")
            public String transportationType;
            @SerializedName("TimeTaken")
            public String timeTaken;
            @SerializedName("Coordinates")
            public List<CoordinatesBean> coordinates;

            public static class CoordinatesBean {
                /**
                 * Provider : Unknown
                 * LNG : 117.1213760376
                 * LAT : 40.140697479248
                 */

                @SerializedName("Provider")
                public String provider;
                @SerializedName("LNG")
                public double lng;
                @SerializedName("LAT")
                public double lat;
            }
        }

        public static class ArrivalOperationsBean implements Serializable {
            /**
             * name : 14:00
             * time : 1535608800
             * timeStr :  2018-08-30T14:00:00+08:00
             */

            @SerializedName("name")
            public String name;
            @SerializedName("time")
            public String time;
            @SerializedName("timeStr")
            public String timeStr;
        }
    }
}

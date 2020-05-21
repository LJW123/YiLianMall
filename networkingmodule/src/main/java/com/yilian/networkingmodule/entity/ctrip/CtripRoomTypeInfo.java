package com.yilian.networkingmodule.entity.ctrip;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaofo on 2018/9/1.
 *         物理房型
 */

public class CtripRoomTypeInfo extends AbstractExpandableItem<CtripRoomTypeInfo.RoomInfosBean> implements MultiItemEntity {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ROOM = 1;
    @SerializedName("typeMinPrice")
    public String minPrice;
    @SerializedName("id")
    public String id;
    @SerializedName("HotelID")
    public String hotelID;
    @SerializedName("RoomTypeID")
    public String roomTypeID;
    @SerializedName("RoomTypeName")
    public String roomTypeName;
    @SerializedName("StandardRoomType")
    public String standardRoomType;
    @SerializedName("NotAllowSmoking")
    public String notAllowSmoking;
    @SerializedName("AreaRange")
    public String areaRange;
    @SerializedName("FloorRange")
    public String floorRange;
    @SerializedName("HasWindow")
    public String hasWindow;
    @SerializedName("ExtraBedFee")
    public String extraBedFee;
    @SerializedName("BathRoomType")
    public String bathRoomType;
    @SerializedName("BroadnetFeeDetail")
    public String broadnetFeeDetail;
    @SerializedName("WirelessBroadnet")
    public String wirelessBroadnet;
    @SerializedName("WiredBroadnet")
    public String wiredBroadnet;
    @SerializedName("MaxOccupancy")
    public String maxOccupancy;
    @SerializedName("MaxAge")
    public String maxAge;
    @SerializedName("MinAge")
    public String minAge;
    @SerializedName("typeBedName")
    public String typeBedName;
    @SerializedName("RoomBedInfos")
    public List<RoomBedInfosBean> roomBedInfos;
    @SerializedName("Facilities")
    public ArrayList<FacilitiesBean> facilities;
    @SerializedName("Pictures")
    public ArrayList<PicturesBean> pictures;
    @SerializedName("Descriptions")
    public List<DescriptionsBean> descriptions;
    @SerializedName("RoomInfos")
    public List<RoomInfosBean> roomInfos;

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return TYPE_HEADER;
    }

    public static class RoomBedInfosBean {
        /**
         * BedInfo : [{"ID":"1144","Name":"沙发床","NumberOfBeds":"1","BedWidth":"1.81"}]
         * ID : 360
         * Name : 大床
         */

        @SerializedName("ID")
        public int roomId;
        @SerializedName("Name")
        public String name;
        @SerializedName("BedInfo")
        public List<bedInfoBean> bedInfo;

        public static class bedInfoBean {
            /**
             * ID : 1144
             * Name : 沙发床
             * NumberOfBeds : 1
             * BedWidth : 1.81
             */

            @SerializedName("ID")
            public String benId;
            @SerializedName("Name")
            public String name;
            @SerializedName("NumberOfBeds")
            public String numberOfBeds;
            @SerializedName("BedWidth")
            public String bedWidth;
        }
    }

    public static class FacilitiesBean {
        /**
         * FacilityItem : [{"ID":"414","Name":"国内长途电话","Status":"1"}]
         * CategoryName : 媒体科技
         */

        @SerializedName("CategoryName")
        public String categoryName;
        @SerializedName("FacilityItem")
        public List<facilityItemBean> facilityItem;

        public static class facilityItemBean {
            /**
             * ID : 414
             * Name : 国内长途电话
             * Status : 1
             */

            @SerializedName("ID")
            public String id;
            @SerializedName("Name")
            public String name;
            @SerializedName("Status")
            public String status;
        }
    }

    public static class PicturesBean implements Serializable {
        /**
         * ExtraAttribute : {"Resolution":"2700/1800","Rank":"112","Sharpness":"12","AestheticScore":"47.206","Source":"ELONG"}
         * ID : 9315844
         * Type : 6
         * Caption : 行政套房
         * URL : https://dimg12.c-ctrip.com/images/20060k000000bnuho3961_R_550_412.jpg
         */

        @SerializedName("ExtraAttribute")
        public extraAttributeBean extraAttribute;
        @SerializedName("ID")
        public String id;
        @SerializedName("Type")
        public String type;
        @SerializedName("Caption")
        public String caption;
        @SerializedName("URL")
        public String url;

        public static class extraAttributeBean {
            /**
             * Resolution : 2700/1800
             * Rank : 112
             * Sharpness : 12
             * AestheticScore : 47.206
             * Source : ELONG
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

    public static class DescriptionsBean {
        /**
         * Text : 享有苏州河及长风公园美景，一大床房一客厅
         * Category : 3
         */

        @SerializedName("Text")
        public String text;
        @SerializedName("Category")
        public String category;
    }

    /**
     * 售卖房型
     */
    public static class RoomInfosBean implements MultiItemEntity, Serializable {
        public ArrayList<CtripHotelDetailEntity.DataBean.PoliciesBean> policies;
        public ArrayList<FacilitiesBean> facilities;

        public ArrayList<PicturesBean> pictures;

        @SerializedName("id")
        public String id;
        @SerializedName("HotelID")
        public String hotelID;
        @SerializedName("RoomTypeID")
        public String roomTypeID;
        @SerializedName("RoomID")
        public String roomID;
        @SerializedName("RoomName")
        public String roomName;
        @SerializedName("RoomQuantity")
        public String roomQuantity;
        @SerializedName("MaxOccupancy")
        public int maxOccupancy;
        @SerializedName("CanFGToPP")
        public String canFGToPP;
        @SerializedName("AreaRange")
        public String areaRange;
        @SerializedName("FloorRange")
        public String floorRange;
        @SerializedName("HasWindow")
        public String hasWindow;
        @SerializedName("ExtraBedFee")
        public String extraBedFee;
        @SerializedName("IsHourlyRoom")
        public String isHourlyRoom;
        @SerializedName("IsFromAPI")
        public String isFromAPI;
        @SerializedName("IsSupported")
        public String isSupported;
        @SerializedName("DepositRatio")
        public String depositRatio;
        @SerializedName("NotAllowSmoking")
        public String notAllowSmoking;
        @SerializedName("BroadnetFeeDetail")
        public String broadnetFeeDetail;
        @SerializedName("WirelessBroadnet")
        public int wirelessBroadnet;
        @SerializedName("WiredBroadnet")
        public int wiredBroadnet;
        @SerializedName("Descriptions")
        public ArrayList<DescriptionsBean> descriptions;
        @SerializedName("Status")
        public String status;
        @SerializedName("StatusChangeLastTime")
        public Object statusChangeLastTime;
        @SerializedName("IsInstantConfirm")
        public boolean isInstantConfirm;
        @SerializedName("BedIDs")
        public String bedIDs;
        @SerializedName("BedName")
        public String bedName;
        @SerializedName("priceInfo")
        public PriceInfoBean priceInfo;
        @SerializedName("RoomBedInfos")
        public List<RoomBedInfosBeanX> roomBedInfos;
        @SerializedName("BookingRules")
        public List<BookingRulesBean> bookingRules;

        public static class PriceInfoBean {
            /**
             * Start : 0001-01-01T00:00:00.0000000
             * End : 2018-09-30T00:00:00.0000000+08:00
             * PenaltyAmount : 1973
             * CancelPolicyInfos : 1
             * HoldTime : 0
             * LatestReserveTime : 1538233200
             * PayType : PP
             * RatePlanCategory : 501
             * IsCanReserve : true
             * IsGuarantee : false
             * IsInstantConfirm  : false
             * RemainingRooms : 10+
             * ExclusiveAmount : 1973
             * InclusiveAmount : 1973
             * ReturnBean : 98.65
             * NumberOfBreakfast : 0
             * DailyExclusiveAmount : 1973
             * DailyInclusiveAmount : 1973
             * DailyReturnBean : 98.65
             * Amount : 0
             * originPriceInfo : [{"EffectiveDate":"2018-09-29","NumberOfBreakfast":0,"DailyExclusiveAmount":1973,"DailyInclusiveAmount":1973}]
             */

            @SerializedName("Start")
            public String start;
            @SerializedName("End")
            public String end;
            @SerializedName("PenaltyAmount")
            public String penaltyAmount;
            @SerializedName("CancelPolicyInfos")
            public int cancelPolicyInfos;
            @SerializedName("HoldTime")
            public String holdTime;
            @SerializedName("LatestReserveTime")
            public String latestReserveTime;
            @SerializedName("PayType")
            public String payType;
            @SerializedName("RatePlanCategory")
            public String ratePlanCategory;
            @SerializedName("IsCanReserve")
            public boolean isCanReserve;
            @SerializedName("IsGuarantee")
            public boolean isGuarantee;
            @SerializedName("IsInstantConfirm")
            public boolean isInstantConfirm;
            @SerializedName("RemainingRooms")
            public String remainingRooms;
            @SerializedName("ExclusiveAmount")
            public String exclusiveAmount;
            @SerializedName("InclusiveAmount")
            public String inclusiveAmount;
            @SerializedName("ReturnBean")
            public String returnBean;
            @SerializedName("NumberOfBreakfast")
            public int numberOfBreakfast;
            @SerializedName("DailyExclusiveAmount")
            public String dailyExclusiveAmount;
            @SerializedName("DailyInclusiveAmount")
            public String dailyInclusiveAmount;
            @SerializedName("DailyReturnBean")
            public String dailyReturnBean;
            @SerializedName("Amount")
            public String amount;
            @SerializedName("originPriceInfo")
            public List<OriginPriceInfoBean> originPriceInfo;

            public static class OriginPriceInfoBean {
                /**
                 * EffectiveDate : 2018-09-29
                 * NumberOfBreakfast : 0
                 * DailyExclusiveAmount : 1973
                 * DailyInclusiveAmount : 1973
                 */

                @SerializedName("EffectiveDate")
                public String effectiveDate;
                @SerializedName("NumberOfBreakfast")
                public String numberOfBreakfast;
                @SerializedName("DailyExclusiveAmount")
                public String dailyExclusiveAmount;
                @SerializedName("DailyInclusiveAmount")
                public String dailyInclusiveAmount;
            }
        }

        public static class RoomBedInfosBeanX {
            /**
             * BedInfo : [{"ID":"1144","Name":"沙发床","NumberOfBeds":"1","BedWidth":"1.81"}]
             * ID : 360
             * Name : 大床
             */

            @SerializedName("ID")
            public String id;
            @SerializedName("Name")
            public String name;
            @SerializedName("BedInfo")
            public List<bedInfoBeanX> bedInfo;

            public static class bedInfoBeanX {
                /**
                 * ID : 1144
                 * Name : 沙发床
                 * NumberOfBeds : 1
                 * BedWidth : 1.81
                 */

                @SerializedName("ID")
                public String id;
                @SerializedName("Name")
                public String name;
                @SerializedName("NumberOfBeds")
                public String numberOfBeds;
                @SerializedName("BedWidth")
                public String bedWidth;
            }
        }

        public static class BookingRulesBean {
            @SerializedName("BookingOffsets")
            public List<?> bookingOffsets;
            @SerializedName("LengthsOfStay")
            public List<?> lengthsOfStay;
            @SerializedName("TimeLimitInfo")
            public List<?> timeLimitInfo;
        }        @Override
        public int getItemType() {
            return TYPE_ROOM;
        }




    }
}

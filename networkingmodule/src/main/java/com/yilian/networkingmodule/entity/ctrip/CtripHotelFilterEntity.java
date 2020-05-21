package com.yilian.networkingmodule.entity.ctrip;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaofo on 2018/9/17.
 */
// extends HttpResultBean
public class CtripHotelFilterEntity extends HttpResultBean {


    /**
     * data : {"opeartions":{"NumberOfBreakfast":{"NumberOfBreakfast":-1,"Name":"含早餐","key":"NumberOfBreakfast"},"RoomBedInfos":[{"ID":360,"Name":"大床","key":"RoomBedInfos"},{"ID":361,"Name":"双床","key":"RoomBedInfos"}],"IsInstantConfirm":{"IsInstantConfirm":true,"Name":"立即确认"},"CancelPolicyInfos":{"CancelPolicyInfos":0,"Name":"免费取消"}},"RoomBedInfos":[{"ID":360,"Name":"大床","key":"RoomBedInfos"},{"ID":361,"Name":"双床","key":"RoomBedInfos"},{"ID":362,"Name":"单人床","key":"RoomBedInfos"},{"ID":363,"Name":"多张床","key":"RoomBedInfos"}],"NumberOfBreakfast":[{"NumberOfBreakfast":-1,"Name":"含早餐","key":"NumberOfBreakfast"},{"NumberOfBreakfast":1,"Name":"单份早餐","key":"NumberOfBreakfast"},{"NumberOfBreakfast":2,"Name":"双份早餐","key":"NumberOfBreakfast"}],"Broadnet":[{"WirelessBroadnet":"2","Name":"免费WIFI上网","key":"Broadnet","WiredBroadnet":"2"},{"WiredBroadnet":"2","Name":"免费有限宽带","key":"Broadnet"}]}
     */
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * opeartions : {"NumberOfBreakfast":{"NumberOfBreakfast":-1,"Name":"含早餐","key":"NumberOfBreakfast"},"RoomBedInfos":[{"ID":360,"Name":"大床","key":"RoomBedInfos"},{"ID":361,"Name":"双床","key":"RoomBedInfos"}],"IsInstantConfirm":{"IsInstantConfirm":true,"Name":"立即确认"},"CancelPolicyInfos":{"CancelPolicyInfos":0,"Name":"免费取消"}}
         * RoomBedInfos : [{"ID":360,"Name":"大床","key":"RoomBedInfos"},{"ID":361,"Name":"双床","key":"RoomBedInfos"},{"ID":362,"Name":"单人床","key":"RoomBedInfos"},{"ID":363,"Name":"多张床","key":"RoomBedInfos"}]
         * NumberOfBreakfast : [{"NumberOfBreakfast":-1,"Name":"含早餐","key":"NumberOfBreakfast"},{"NumberOfBreakfast":1,"Name":"单份早餐","key":"NumberOfBreakfast"},{"NumberOfBreakfast":2,"Name":"双份早餐","key":"NumberOfBreakfast"}]
         * Broadnet : [{"WirelessBroadnet":"2","Name":"免费WIFI上网","key":"Broadnet"},{"WiredBroadnet":"2","Name":"免费有限宽带","key":"Broadnet"}]
         */
        @SerializedName("opeartions")
        public OpeartionsBean opeartions;
        @SerializedName("RoomBedInfos")
        public List<RoomBedInfosBeanX> RoomBedInfos;
        @SerializedName("NumberOfBreakfast")
        public List<NumberOfBreakfastBeanX> NumberOfBreakfast;
        @SerializedName("Broadnet")
        public List<BroadnetBean> Broadnet;
        @SerializedName("pSort")
        public PSortBean pSort;
        public DataBean(OpeartionsBean opeartions, List<RoomBedInfosBeanX> roomBedInfos, List<NumberOfBreakfastBeanX> numberOfBreakfast, List<BroadnetBean> broadnet) {
            this.opeartions = opeartions;
            RoomBedInfos = roomBedInfos;
            NumberOfBreakfast = numberOfBreakfast;
            Broadnet = broadnet;
        }



        public static class PSortBean {
            /**
             * sorts : [{"sorts":"0,150","title":"¥150以下"},{"sorts":"150,300","title":"¥150-300"},{"sorts":"300,450","title":"¥300-450"},{"sorts":"450,600","title":"¥450-600"},{"sorts":"600,1000","title":"¥600-1000"},{"sorts":"1000,100000000","title":"¥1000以上"}]
             * title : 价格
             * price : 0,1000
             */
            @SerializedName("title")
            public String title;
            @SerializedName("price")
            public String price;
            @SerializedName("sorts")
            public List<SortsBean> sorts;

            public static class SortsBean {
                /**
                 * sorts : 0,150
                 * title : ¥150以下
                 */
                @SerializedName("sorts")
                public String sorts;
                @SerializedName("title")
                public String title;
            }
        }
        public static class OpeartionsBean implements Serializable {
            /**
             * NumberOfBreakfast : {"NumberOfBreakfast":-1,"Name":"含早餐","key":"NumberOfBreakfast"}
             * RoomBedInfos : [{"ID":360,"Name":"大床","key":"RoomBedInfos"},{"ID":361,"Name":"双床","key":"RoomBedInfos"}]
             * IsInstantConfirm : {"IsInstantConfirm":true,"Name":"立即确认"}
             * CancelPolicyInfos : {"CancelPolicyInfos":0,"Name":"免费取消"}
             */
            @SerializedName("NumberOfBreakfast")
            public NumberOfBreakfastBean NumberOfBreakfast;
            @SerializedName("IsInstantConfirm")
            public IsInstantConfirmBean IsInstantConfirm;
            @SerializedName("CancelPolicyInfos")
            public CancelPolicyInfosBean CancelPolicyInfos;
            @SerializedName("RoomBedInfos")
            public List<RoomBedInfosBean> RoomBedInfos;

            public OpeartionsBean(NumberOfBreakfastBean numberOfBreakfast, IsInstantConfirmBean isInstantConfirm, CancelPolicyInfosBean cancelPolicyInfos, List<RoomBedInfosBean> roomBedInfos) {
                NumberOfBreakfast = numberOfBreakfast;
                IsInstantConfirm = isInstantConfirm;
                CancelPolicyInfos = cancelPolicyInfos;
                RoomBedInfos = roomBedInfos;
            }


            public static class NumberOfBreakfastBean extends IsChecked{
                /**
                 * NumberOfBreakfast : -1
                 * Name : 含早餐
                 * key : NumberOfBreakfast
                 */
                @SerializedName("NumberOfBreakfast")
                public int NumberOfBreakfast;
                @SerializedName("Name")
                public String Name;
                @SerializedName("key")
                public String key;

            }

            public static class IsInstantConfirmBean extends IsChecked{
                /**
                 * IsInstantConfirm : true
                 * Name : 立即确认
                 */
                @SerializedName("IsInstantConfirm")
                public boolean IsInstantConfirm;
                @SerializedName("Name")
                public String Name;

            }

            public static class CancelPolicyInfosBean extends IsChecked{
                /**
                 * CancelPolicyInfos : 0
                 * Name : 免费取消
                 */
                @SerializedName("CancelPolicyInfos")
                public int CancelPolicyInfos;
                @SerializedName("Name")
                public String Name;

            }

            public static class RoomBedInfosBean extends IsChecked{
                /**
                 * ID : 360
                 * Name : 大床
                 * key : RoomBedInfos
                 */
                @SerializedName("ID")
                public int ID;
                @SerializedName("Name")
                public String Name;
                @SerializedName("key")
                public String key;

            }
        }

        public static class RoomBedInfosBeanX extends IsChecked{
            /**
             * ID : 360
             * Name : 大床
             * key : RoomBedInfos
             */
            @SerializedName("ID")
            public int ID;
            @SerializedName("Name")
            public String Name;
            @SerializedName("key")
            public String key;

        }

        public static class NumberOfBreakfastBeanX extends IsChecked{
            /**
             * NumberOfBreakfast : -1
             * Name : 含早餐
             * key : NumberOfBreakfast
             */
            @SerializedName("NumberOfBreakfast")
            public int NumberOfBreakfast;
            @SerializedName("Name")
            public String Name;
            @SerializedName("key")
            public String key;

        }

        public static class BroadnetBean extends IsChecked{
            /**
             * WirelessBroadnet : 2
             * Name : 免费WIFI上网
             * key : Broadnet
             * WiredBroadnet : 2
             */
            @SerializedName("WirelessBroadnet")
            public String WirelessBroadnet;
            @SerializedName("Name")
            public String Name;
            @SerializedName("key")
            public String key;
            @SerializedName("WiredBroadnet")
            public String WiredBroadnet;

        }
    }

    public static class IsChecked {
        public boolean ischecked;
        public String textView;
    }


}

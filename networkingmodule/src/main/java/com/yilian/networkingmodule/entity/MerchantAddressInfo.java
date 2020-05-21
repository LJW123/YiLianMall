package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/6/15 0015.
 */

public class MerchantAddressInfo extends BaseEntity {

    /**
     * info : {"merchant_province":"12","merchant_city":"123","merchant_county":"1234","merchant_id":"2","merchant_address":"测试详细地址","merchant_longitude":"456","merchant_latitude":"123","region":{"province":{"id":"12","name":"黑龙江"},"city":{"id":"123","name":"保亭"},"county":{"id":"1234","name":"桥西区"}}}
     */

    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean {
        /**
         * merchant_province : 12
         * merchant_city : 123
         * merchant_county : 1234
         * merchant_id : 2
         * merchant_address : 测试详细地址
         * merchant_longitude : 456
         * merchant_latitude : 123
         * region : {"province":{"id":"12","name":"黑龙江"},"city":{"id":"123","name":"保亭"},"county":{"id":"1234","name":"桥西区"}}
         */

        @SerializedName("merchant_province")
        public String merchantProvince;
        @SerializedName("merchant_city")
        public String merchantCity;
        @SerializedName("merchant_county")
        public String merchantCounty;
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("merchant_address")
        public String merchantAddress;
        @SerializedName("merchant_longitude")
        public String merchantLongitude;
        @SerializedName("merchant_latitude")
        public String merchantLatitude;
        @SerializedName("region")
        public RegionBean region;

        public static class RegionBean {
            /**
             * province : {"id":"12","name":"黑龙江"}
             * city : {"id":"123","name":"保亭"}
             * county : {"id":"1234","name":"桥西区"}
             */

            @SerializedName("province")
            public ProvinceBean province;
            @SerializedName("city")
            public CityBean city;
            @SerializedName("county")
            public CountyBean county;

            public static class ProvinceBean {
                /**
                 * id : 12
                 * name : 黑龙江
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
            }

            public static class CityBean {
                /**
                 * id : 123
                 * name : 保亭
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
            }

            public static class CountyBean {
                /**
                 * id : 1234
                 * name : 桥西区
                 */

                @SerializedName("id")
                public String id;
                @SerializedName("name")
                public String name;
            }
        }
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lefenandroid on 16/5/11.
 */
public class Location extends BaseEntity {

    public location location;

    public class location {
        public String lat; // 经度

        public String lng; // 纬度

        public String nation; // 国名

        @SerializedName("province_name")
        public String provinceName; // 省名称

        @SerializedName("city_name")
        public String cityName; // 市名称

        @SerializedName("county_name")
        public String countyName; // 区名称

        public String address; // 完整的地址信息

        @SerializedName("province_id")
        public String provinceId; // 省

        @SerializedName("city_id")
        public String cityId; // 市

        @SerializedName("county_id")
        public String countyId; // 区
    }
}

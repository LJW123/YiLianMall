package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lefenandroid on 16/5/11.
 */
public class CityList extends BaseEntity {

    public ArrayList<province> cities;

    public class province{
        @SerializedName("region_id")
        public String regionId;

        @SerializedName("parent_id")
        public String parentId;

        @SerializedName("region_name")
        public String regionName; //

        @SerializedName("region_type")
        public String regionType;

        @SerializedName("agency_id")
        public String agencyId;

        public ArrayList<ProvinceList.province> counties;
    }
}

package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author zhaiyaohua
 *         携程 城市数据
 */
public class CtripSiteCityEntity extends HttpResultBean {

    @SerializedName("data")
    public List<City> data;
    @SerializedName("hotCity")
    public List<HotCity> hotCity;


    public static class City {
        @SerializedName("city")
        public String city;
        @SerializedName("cityname")
        public String cityname;
    }

    public static class HotCity {
        @SerializedName("city")
        public String city;
        @SerializedName("cityname")
        public String cityname;
    }

}

package com.yilian.networkingmodule.entity.ctrip;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author zhaiyaohua
 *         携程 城市数据
 */
public class CtripSiteCityByDistrictsEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;


    public static class DataBean {
        @SerializedName("city")
        public String city;
        @SerializedName("cityname")
        public String cityname;
        @SerializedName("location")
        public String location;
        @SerializedName("locationname")
        public String locationname;
    }


}

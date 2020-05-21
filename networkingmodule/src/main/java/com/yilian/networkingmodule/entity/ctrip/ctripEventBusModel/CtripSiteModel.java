package com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel;

import java.io.Serializable;

/**
 * 携程  目的地选择
 * Created by @author Zg on 2018/8/29.
 */

public class CtripSiteModel implements Serializable {

    /**
     * 城市id
     */
    public String cityId;
    public String cityName;
    /**
     * 区县id 没有传空字符串
     */
    public String countryId;
    public String countryName;
    /**
     * 经纬度 未开启定位/选择其他城市就传0
     */
    public String gdLat;
    /**
     * 经纬度 未开启定位/选择其他城市就传0
     */
    public String gdLng;


    public CtripSiteModel(String cityId,String cityName,String countryId,String countryName,String gdLat, String gdLng) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.gdLat = gdLat;
        this.gdLng = gdLng;
    }

}

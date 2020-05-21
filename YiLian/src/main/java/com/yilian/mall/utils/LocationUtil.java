package com.yilian.mall.utils;/**
 * Created by ASUS on 2016/9/13 0013.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Created by  on 2016/9/13 0013
 */
public class LocationUtil {

    /**
     * 获取的字符串用于存放用户地理位置信息，以便JS调用getUserLocationInfo()方法获得用户地理位置，经纬度暂时无效(设置为两个空字符串)
     *
     * @param cityId
     * @param countyId
     * @param provinceId
     * @return
     */
    public static String getJSLocationInfo(String cityId, String countyId, String provinceId) {
        Map location = new HashMap();
        location.put("lat", "");
        location.put("lng", "");
        location.put("city_id", cityId);
        location.put("county_id", countyId);
        location.put("province_id", provinceId);
        return "{\"lat\":\"\",\"lng\":\"\",\"city_id\":" + cityId + ",\"county_id\":" + countyId + ",\"province_id\":" + provinceId + "}";
    }

    /**
     * 获取携程用户地理位置信息经纬度/城市/区域/商业区
     *
     * @param city     城市Id
     * @param zoneid   商业区Id 没有为空
     * @param location 区县id 没有为0
     * @param gdLat    纬度 未开启定位/选择其他城市为0
     * @param gdLng    经度 未开启定位/选择其他城市为0
     * @return
     */
    public static String getXcUserLocationInfo(String city,String cityname,String zoneid, String location, String gdLat, String gdLng) {
        Map map = new HashMap();
        map.put("city", city);
        map.put("cityname", cityname);
        map.put("zoneid", zoneid);
        map.put("location", location);
        map.put("gdLat", gdLat);
        map.put("gdLng", gdLng);
        return GsonUtil.setBeanToJson(map);
    }
}

package com.yilian.mall.utils;

/**
 * Created by Ray_L_Pain on 2016/12/8 0008.
 * 计算距离的工具类  经-lng  纬-lat
 */

public class DistanceUtil {


    /**
     * 得到两点间的距离 米
     *
     * @param lat1 第一点纬度
     * @param lng1 第一点经度
     * @param lat2 第二点纬度
     * @param lng2 第二点经度
     * @return
     */
    public static double getDistanceOfMeter(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 地球半径：6378.137KM
     */
    private static double EARTH_RADIUS = 6378.137;
}

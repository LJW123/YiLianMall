package com.yilian.mall.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.yilian.mall.MyApplication;

import java.util.List;

/**
 * Created by liuyuqi on 2016/12/10 0010.
 *  通过手机当前的位置获取经纬度
 */
public class DistanceFromPhoneUtils {

    private static double longitude;
    private static double latitude;
    //传入显示位置的TextView
   public static void getDistanceFromPhone(){
       Context mContext = MyApplication.getInstance();

       //获取位置的管理器
       LocationManager locationManager= (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
      //获取所有可用的位置提供器
       List<String> allProviders = locationManager.getAllProviders();

       String locationProvider = null;
       if (allProviders.contains(LocationManager.GPS_PROVIDER)){
           locationProvider = LocationManager.GPS_PROVIDER;
       }else if (allProviders.contains(LocationManager.NETWORK_PROVIDER)){
           locationProvider=LocationManager.NETWORK_PROVIDER;
       }else{
           Toast.makeText(mContext, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
       }
       //获取Location
       Location location = locationManager.getLastKnownLocation(locationProvider);

       if (location!=null){
           //获取经纬度
           longitude = location.getLongitude();
           latitude = location.getLatitude();
       }else{
           Toast.makeText(mContext, "获取位置失败", Toast.LENGTH_SHORT).show();
       }
   }

    //根据经纬度获取距离返回的单位是KM
    public static float getDistanceOfMeter(String  lat1, String  lng1) {
        double lat= Float.parseFloat(lat1);
        double lng = Float.parseFloat(lng1);

        double radLat1 = rad(lat);
        double radLat2 = rad(latitude);
        double a = radLat1 - radLat2;
        double b = rad(lng) - rad(longitude);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        /**
         * s返回单位是米 换算成KM
         */
        return (float) s/10000;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 地球半径：6378.137KM
     */
    private static double EARTH_RADIUS = 6378.137;

}

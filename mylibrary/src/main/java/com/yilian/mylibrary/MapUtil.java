package com.yilian.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.amap.api.maps2d.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray_L_Pain on 2018/2/24 0024.
 */

public class MapUtil {

    /**
     * 检查手机上是否安装制定的软件
     * packageName app包名
     */
    public static boolean isAvilible(Context mContext, String packageName) {
        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> infoList = pm.getInstalledPackages(0);
        List<String> packageNameList = new ArrayList<>();
        if (null != infoList) {
            for (int i = 0; i < infoList.size(); i++) {
                String packName = infoList.get(i).packageName;
                packageNameList.add(packName);
            }
        }

        return packageNameList.contains(packageName);
    }

    /**
     *  跳转高德地图的导航
     */
    public static void jumpToGaoDe(Context mContext, double latitude, double longitude) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://navi?sourceApplication=益联益家&lat=" + latitude + "&lon=" + longitude + "&dev=0&style=0"));
        intent.setPackage(Constants.PACKAGE_AMAP_MAP);
        mContext.startActivity(intent);
    }

    /**
     * 跳转百度地图的导航
     */
    public static void jumpToBaidu(Context mContext, MarkerOptions markerOption) {
        Intent i1 = new Intent();
        double[] position = GPSUtil.gcj02_To_Bd09(markerOption.getPosition().latitude, markerOption.getPosition().longitude);
        i1.setData(Uri.parse("baidumap://map/geocoder?location=" + position[0] + "," + position[1]));
        mContext.startActivity(i1);
    }

    /**
     * 跳转腾讯地图的导航
     */
    public static void jumpToTencent(Context mContext, double myLatitude, double myLongitude, double latitude, double longitude, String address) {
        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=我的位置&fromcoord=" + myLatitude + "," + myLongitude + "&to=" + address + "&tocoord=" + latitude + "," + longitude + "&referer=益联益家"));
        intent.setPackage(Constants.PACKAGE_TENCENT_MAP);
        mContext.startActivity(intent);
    }

    public static String getWebBaiduMapUri(String originLat, String originLon, String originName, String desLat, String desLon, String destination, String region, String appName) {
        String uri = "http://api.map.baidu.com/direction?origin=latlng:%1$s,%2$s|name:%3$s" +
                "&destination=latlng:%4$s,%5$s|name:%6$s&mode=driving&region=%7$s&output=html" +
                "&src=%8$s";
        return String.format(uri, originLat, originLon, originName, desLat, desLon, destination, region, appName);
    }
}

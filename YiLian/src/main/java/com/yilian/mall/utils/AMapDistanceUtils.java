package com.yilian.mall.utils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yilian.mall.MyApplication;
import com.yilian.mall.entity.DistanceEntity;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by liuyuqi on 2017/3/9 0009.
 * 通过高德地图计算当前的距离值
 */
public class  AMapDistanceUtils<T extends DistanceEntity> {

    public static List getListDistanceFromAMap(String latitude, String longitude, List< ? extends DistanceEntity > List){

        //获得当前的坐标
        double latitude1 = ((MyApplication) MyApplication.getInstance().getApplicationContext()).getLatitude();
        double longitude1 = ((MyApplication) MyApplication.getInstance().getApplicationContext()).getLongitude();
        final LatLng latLngLocate = new LatLng(latitude1, longitude1);
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if (latitude1 > 0.0 && longitude1 > 0.0) {
            if (List.size() > 1) {
                Collections.sort(List, new Comparator<DistanceEntity>() {
                    @Override
                    public int compare(DistanceEntity lhs, DistanceEntity rhs) {

                        double[] latlnglhs = CommonUtils.bd2gcj02(Double.parseDouble(latitude),
                                Double.parseDouble(longitude));
                        double[] latlngrhs = CommonUtils.bd2gcj02(Double.parseDouble(latitude),
                                Double.parseDouble(longitude));
                        LatLng latLnglhs = new LatLng(latlnglhs[0], latlnglhs[1]);
                        LatLng latLngrhs = new LatLng(latlngrhs[0], latlngrhs[1]);
                        float distancelhs = AMapUtils.calculateLineDistance(latLnglhs,
                                latLngLocate);
                        float distancerhs = AMapUtils.calculateLineDistance(latLngrhs,
                                latLngLocate);
                        lhs.distance = decimalFormat.format(distancelhs / 1000);
                        rhs.distance = decimalFormat.format(distancerhs / 1000);
                        float diff = distancelhs - distancerhs;
                        if (diff > 0) {
                            return 1;
                        } else if (diff < 0) {
                            return -1;
                        }
                        return 0;
                    }
                });
            } else {
                //单条数据直接计算距离
                double[] latlng = CommonUtils.bd2gcj02(Double.parseDouble(latitude),
                        Double.parseDouble(longitude));
                LatLng latLng = new LatLng(latlng[0], latlng[1]);
                float distance = AMapUtils.calculateLineDistance(latLng,
                        latLngLocate);
                List.get(0).distance = decimalFormat.format(distance / 1000);

                }
            }
            return List;
        }

    /**
     * 计算距离
     * @param latitude
     * @param longitude
     * @return 单位米
     */
    public static float getSingleDistance2(String latitude, String longitude){
        //获得当前的坐标
        double latitude1 = ((MyApplication) MyApplication.getInstance().getApplicationContext()).getLatitude();
        double longitude1 = ((MyApplication) MyApplication.getInstance().getApplicationContext()).getLongitude();
        final LatLng latLngLocate = new LatLng(latitude1, longitude1);
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double[] latlng = CommonUtils.bd2gcj02(Double.parseDouble(latitude),
                Double.parseDouble(longitude));
        LatLng latLng = new LatLng(latlng[0], latlng[1]);
        float distance = AMapUtils.calculateLineDistance(latLng,
                latLngLocate);
        Logger.i("distanceStr  "+distance);
        return distance ;
    }

}

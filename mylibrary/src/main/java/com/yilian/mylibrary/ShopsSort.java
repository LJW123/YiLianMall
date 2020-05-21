package com.yilian.mylibrary;/**
 * Created by  on 2017/3/28 0028.
 */

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yilian.mylibrary.entity.LocationEntity;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by  on 2017/3/28 0028.
 * 把商家按离当前位置 由近及远 进行排序，并为所有参与排序的商家 设置离当前位置的具体长度。
 * 注意：使用该方法的类都得集成LocationEntity（里面有共同的三个属性 经、纬度和距离）
 */
public class ShopsSort extends BaseEntity {

    public static <T extends LocationEntity> void sort(Context context, List<T> copyComboList) {

        //通过高德地图计算位置并返回一个排好序的集合
        final double latitude = NumberFormat.convertToDouble(PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LAT, context), 0d);
//        Logger.i("定位log  排序使用 获取的经度：" + latitude);
        final double longitude = NumberFormat.convertToDouble(PreferenceUtils.readStrConfig(Constants.SELF_LOCATION_LNG, context), 0d);
//        Logger.i("定位log  排序使用 获取的经度：" + longitude);
        final LatLng latLngLocate = new LatLng(latitude, longitude);
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if (latitude > 0.0 && longitude > 0.0) {
            if (copyComboList.size() > 1) {
                Collections.sort(copyComboList, new Comparator<T>() {
                    @Override
                    public int compare(T lhs, T rhs) {
                        // TODO Auto-generated method stub
                        if (TextUtils.isEmpty(lhs.merchantLatitude)) {
                            lhs.merchantLatitude = "0";
                        }
                        if (TextUtils.isEmpty(lhs.merchantLongitude)) {
                            lhs.merchantLongitude = "0";
                        }
                        if (TextUtils.isEmpty(rhs.merchantLatitude)) {
                            rhs.merchantLatitude = "0";
                        }
                        if (TextUtils.isEmpty(rhs.merchantLongitude)) {
                            rhs.merchantLongitude = "0";
                        }
                        double[] latlnglhs = CommonUtils.bd2gcj02(Double.parseDouble(lhs.merchantLatitude),
                                Double.parseDouble(lhs.merchantLongitude));
                        double[] latlngrhs = CommonUtils.bd2gcj02(Double.parseDouble(rhs.merchantLatitude),
                                Double.parseDouble(rhs.merchantLongitude));
                        LatLng latLnglhs = new LatLng(latlnglhs[0], latlnglhs[1]);
                        LatLng latLngrhs = new LatLng(latlngrhs[0], latlngrhs[1]);
                        float distancelhs = AMapUtils.calculateLineDistance(latLnglhs,
                                latLngLocate);
                        float distancerhs = AMapUtils.calculateLineDistance(latLngrhs,
                                latLngLocate);
                        //格式化客户端计算出的距离
                        if (distancelhs > 1000) {
                            lhs.distance = decimalFormat.format(distancelhs / 1000) + "km";
                        } else {
                            lhs.distance = (int) distancelhs + "m";
                        }
                        if (distancerhs > 1000) {
                            rhs.distance = decimalFormat.format(distancerhs / 1000) + "km";
                        } else {
                            rhs.distance = (int) distancerhs + "m";
                        }
                        //                        格式化服务器返回的距离
                        if (lhs.serviceMerDistance > 1000) {
                            lhs.formatServiceMerDistance = decimalFormat.format(lhs.serviceMerDistance / 1000) + "km";
                        } else {
                            lhs.formatServiceMerDistance = (int) lhs.serviceMerDistance + "m";
                        }
                        if (rhs.serviceMerDistance > 1000) {
                            rhs.formatServiceMerDistance = decimalFormat.format(rhs.serviceMerDistance / 1000) + "km";
                        } else {
                            rhs.formatServiceMerDistance = (int) rhs.serviceMerDistance + "m";
                        }
                        float diff = distancelhs - distancerhs;
//                        取消客户端按照距离远近排序
                        if (diff > 0) {
                            return 0;
                        } else if (diff < 0) {
                            return 0;
                        }
                        return 0;
                    }
                });
            } else if (copyComboList.size() == 1) {
                T t = copyComboList.get(0);
                if (!TextUtils.isEmpty(t.merchantLatitude) || !TextUtils.isEmpty(t.merchantLongitude)) {
                    double[] latlng = CommonUtils.bd2gcj02(Double.parseDouble(t.merchantLatitude),
                            Double.parseDouble(t.merchantLongitude));
                    LatLng latLng = new LatLng(latlng[0], latlng[1]);
                    float distance = AMapUtils.calculateLineDistance(latLng,
                            latLngLocate);
                    //格式化客户端计算出的距离
                    if (distance > 1000) {
                        t.distance = decimalFormat.format(distance / 1000) + "Km";
                    } else {
                        t.distance = (int) distance + "m";
                    }
                    //                        格式化服务器返回的距离
                    if (t.serviceMerDistance > 1000) {
                        t.formatServiceMerDistance = decimalFormat.format(t.serviceMerDistance / 1000) + "Km";
                    } else {
                        t.formatServiceMerDistance = (int) t.serviceMerDistance + "m";
                    }
                }
            } else {
//                说明集合长度为0 不做任何操作
            }
        }

    }
}

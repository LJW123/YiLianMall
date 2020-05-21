package com.yilian.mall.utils;

import com.amap.api.location.AMapLocation;
import com.lidroid.xutils.exception.HttpException;
import com.yilian.mall.entity.Location;

/**
 * Created by  on 2017/7/13 0013.
 */

public interface AMapLocationSuccessListener {
    /**
     * 定位成功监听
     * @param aMapLocation
     * @param location 根据定位获取的经纬度，获取的地址信息
     */
    void amapLocationSuccessListener(AMapLocation aMapLocation, Location.location location);

    void amapLocationFailureListener(HttpException e,String s);
}

package com.yilian.mall.ctrip.bean;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

/**
 * 作者：马铁超 on 2018/9/26 14:45
 */

public class MarketBean {
   public int id;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    public LatLng mPoint;

    /**
     * 标题
     */
    private String title;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

package com.yilian.mylibrary.entity;/**
 * Created by  on 2017/3/28 0028.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/3/28 0028.
 * 该类包含经、纬度和距离三个属性
 */
public class LocationEntity {
    @SerializedName("merchant_latitude")
    public String merchantLatitude;
    @SerializedName("merchant_longitude")
    public String merchantLongitude;
    /**
     * 客户端计算的的商家距离客户端位置的距离
     */
    public String distance;
    /**
     * 服务器返回的商家距离客户端位置的距离
     */
    @SerializedName("mer_distance")
    public double serviceMerDistance;
    /**
     * 格式化后的服务器返回的距离
     */
    public String formatServiceMerDistance;
}

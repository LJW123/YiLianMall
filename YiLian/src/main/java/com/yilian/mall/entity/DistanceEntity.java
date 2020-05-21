package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/3/9 0009.
 */

public class DistanceEntity extends BaseEntity {

    /**
     * 商家地理位置纬度
     */
    @SerializedName("merchant_latitude")
    public String merchantLatitude;

    /**
     * 商家地理位置经度
     */
    @SerializedName("merchant_longitude")
    public String merchantLongitude;

    public String distance;

}

package com.yilian.mall.entity;/**
 * Created by  on 2016/12/9 0009.
 */

import java.io.Serializable;

/**
 * Created by  on 2016/12/9 0009.
 * 自定义位置信息，高德地图的位置信息实现了Parcelable 没有实现Serializable 无法通过SPUtil存取，这里自定义位置类用于搜索地址后将地址信息存储下来，在设置地址界面使用
 */
public class MyPoiItem implements Serializable {
    public String title;
    public double Latitude;
    public double Longitude;
    public String cityCode;
    public String provinceName;//省
    public String cityName;//市
    public String adName;//区


}

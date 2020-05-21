package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2016/10/18 0018.
 * Banner图 实体类
 */

public class JPBannerEntity extends BaseEntity implements Serializable {

    /**
     * banner_type : 0 url ，1 商品id
     * banner_data : 内容
     * image_url : 图片
     * titles : 标题名称
     */

    @SerializedName("banner_type")
    public int JPBannerType;
    @SerializedName("banner_data")
    public String JPBannerData;
    @SerializedName("image_url")
    public String JPImageUrl;
    @SerializedName("titles")
    public String JPTitles;

    @Override
    public String toString() {
        return "JPBannerEntity{" +
                "JPBannerType=" + JPBannerType +
                ", JPBannerData='" + JPBannerData + '\'' +
                ", JPImageUrl='" + JPImageUrl + '\'' +
                ", JPTitles='" + JPTitles + '\'' +
                '}';
    }
}

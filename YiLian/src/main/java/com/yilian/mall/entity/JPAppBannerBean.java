package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2016/10/20 0020.
 */

public class JPAppBannerBean {

    @SerializedName("banner_type")
    public int JPBannerType;
    @SerializedName("banner_data")
    public String JPBannerData;
    @SerializedName("image_url")
    public String JPImageUrl;
    @SerializedName("titles")
    public String JPTitles;
}

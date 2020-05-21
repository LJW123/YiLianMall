package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2016/10/21 0021.
 */

public class IconClassBean implements Serializable {

    @SerializedName("type")
    public int JPType;
    @SerializedName("content")
    public String JPContent;
    @SerializedName("image_url")
    public String JPImageUrl;
    @SerializedName("name")
    public String JPName;
}

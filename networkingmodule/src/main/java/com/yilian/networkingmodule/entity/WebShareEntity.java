package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @atuhor Created by  on 2017/10/28 0028.
 */

public class WebShareEntity {

    /**
     * title : 益联益家
     * content : 我是大家猜呀
     * imageUrl :
     * url :
     */

    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("url")
    public String url;

    @Override
    public String toString() {
        return "WebShareEntity{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

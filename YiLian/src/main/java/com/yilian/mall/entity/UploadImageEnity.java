package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/5/18 0018.
 */

public class UploadImageEnity extends BaseEntity {

    /**
     * filename : merchant/images/20170508/5006_734360_3 - 副本.jpg.jpg
     */

    @SerializedName("filename")
    public String filename;
}

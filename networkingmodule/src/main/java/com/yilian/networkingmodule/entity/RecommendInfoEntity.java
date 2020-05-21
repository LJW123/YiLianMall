package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/6/7 0007.
 */
public class RecommendInfoEntity extends BaseEntity {

    /**
     * code :
     * photo :
     * user_name :
     */

    @SerializedName("photo")
    public String photo;
    @SerializedName("user_name")
    public String userName;
}

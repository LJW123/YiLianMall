package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/3.
 */
public class UserInfoEntity extends BaseEntity {

    @SerializedName("user_id")
    public String user_id;
    @SerializedName("photo")
    public String photo;
    @SerializedName("user_name")
    public String user_name;
}

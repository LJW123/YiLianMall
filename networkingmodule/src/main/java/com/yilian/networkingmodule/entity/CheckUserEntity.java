package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LYQ on 2017/10/18.
 */

public class CheckUserEntity extends BaseEntity{

    @SerializedName("user_name")
    public String userName;//用户昵称
}

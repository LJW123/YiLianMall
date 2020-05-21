package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/4/19 0019.
 */

public class ThirdPartyNewUserEntity extends BaseEntity {

    /**
     * newuser :
     */

    @SerializedName("newrecord")
    public String newUser; //1有记录  2无记录
}

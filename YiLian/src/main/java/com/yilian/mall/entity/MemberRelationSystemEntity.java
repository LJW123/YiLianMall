package com.yilian.mall.entity;/**
 * Created by  on 2017/1/12 0012.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/1/12 0012.
 */
public class MemberRelationSystemEntity extends BaseEntity {

    /**
     * user_name :
     * user_photo :
     * filiale_name :
     * merchant_name :
     */

    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_photo")
    public String userPhoto;
    @SerializedName("filiale_name")
    public String filialeName;
    @SerializedName("merchant_name")
    public String merchantName;
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ASUS on 2017/4/13 0013.
 */

public class AliPayUserIdEntity extends BaseEntity implements Serializable{

    /**
     * data : {"user_id":"2088121659453679"}
     * request : GET /app/account.php?c=alipay_oauth_login&app_id=2016022401161443&source=alipay_wallet&scope=auth_base&auth_code=9813663d047e4112b5ed643b867dWB67
     */

    @SerializedName("data")
    public DataBean data;
    @SerializedName("request")
    public String request;

    public static class DataBean implements Serializable{
        /**
         * user_id : 2088121659453679
         */

        @SerializedName("user_id")
        public String userId;
    }
}

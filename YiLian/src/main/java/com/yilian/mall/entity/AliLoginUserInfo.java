package com.yilian.mall.entity;/**
 * Created by  on 2017/3/3 0003.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/3/3 0003.
 * 用户登录Ali客服系统的账号信息
 */
public class AliLoginUserInfo extends BaseEntity {

    /**
     * data : {"userid":"","password":""}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * userid :
         * password :
         */

        @SerializedName("userid")
        public String userid;
        @SerializedName("password")
        public String password;

        @Override
        public String toString() {
            return "DataBean{" +
                    "userid='" + userid + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginAliIM{" +
                "data=" + data.toString() +
                '}';
    }

}

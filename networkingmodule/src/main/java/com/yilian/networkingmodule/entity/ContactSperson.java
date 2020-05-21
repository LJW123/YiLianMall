package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ASUS on 2017/12/8 0008.
 */

public class ContactSperson extends HttpResultBean {

    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * name : 小雨
         * phone : 18103712073
         * photo : app/images/head/20171124/7569242265311312_9101626_1511495222744environment.jpg
         * userid : 7569242265311312
         */

        @SerializedName("name")
        public String name;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
        @SerializedName("userid")
        public String userid;
    }
}

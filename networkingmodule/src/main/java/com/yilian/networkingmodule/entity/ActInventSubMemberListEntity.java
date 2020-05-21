package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActInventSubMemberListEntity extends HttpResultBean {

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public class DataBean {
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("name")
        public String name;
        @SerializedName("phone")
        public String phone;
        @SerializedName("photo")
        public String photo;
        @SerializedName("regtime")
        public String regtime;
    }

}

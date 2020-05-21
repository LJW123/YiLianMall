package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/2/5 0005.
 */

public class MerchantAddressListEntity extends HttpResultBean {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        @SerializedName("index")
        public String index;
        @SerializedName("contacts")
        public String contacts;
        @SerializedName("phone")
        public String phone;
        @SerializedName("address")
        public String address;
        @SerializedName("full_address")
        public String full_address;
        @SerializedName("time")
        public String time;
        @SerializedName("consumer")
        public String consumer;
        @SerializedName("default_address")
        public int default_address;
        @SerializedName("address_flag")
        public String address_flag;
        @SerializedName("is_del")
        public String is_del;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2018/2/5 0005.
 */

public class MerchantCenterInfoEntity extends HttpResultBean {


    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean {
        @SerializedName("supplier_name")
        public String supplierName;
        @SerializedName("supplier_icon")
        public String supplierIcon;
        @SerializedName("supplier_phone")
        public String supplierPhone;
        @SerializedName("supplier_province")
        public String supplierProvince;
        @SerializedName("supplier_city")
        public String supplierCity;
    }
}

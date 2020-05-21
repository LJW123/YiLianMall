package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/6/13 0013.
 */
public class PayTypeEntity extends BaseEntity{
    /**
     * 支付方式可用
     */
    public static final String PAY_TYPE_ISUSE = "1";
    @SerializedName("data")
    public ArrayList<PayData>  data;

    public class PayData{
        @SerializedName("class_name")
        public String className;
        @SerializedName("class_subtitle")
        public String classSubtitle;
        @SerializedName("icon")
        public String icon;
        @SerializedName("content")
        public String content;
        @SerializedName("isuse")
        public String isuse; //是否可用
        /**
         * 1支付宝 2微信 3微信公共账号 4网银
         */
        @SerializedName("pay_type")
        public String payType;
        public boolean isChecked;
    }

}

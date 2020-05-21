package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * author XiuRenLi on 2016/8/10  PRAY NO BUG
 */

public class PayTypeListEntity extends BaseEntity {

    /**
     * class_name : 支付类型名称 例如：支付宝
     * class_subtitle : 支付类型简介 例如：不需要别人给予，想要的触手可及
     * icon : 支付类型图标  app/icon/20160809/0.png
     * content : // 基础url+content+?token=''"&device_index=""&type=""&money="" type 0 乐分币充值 1乐享币直充 money以分为单位 pay_type 支付类型
     * isuse : 1  //支付方式是否可用  0 不可用  1可用
     * pay_type ://支付类型标识   1支付宝 2微信
     */

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        @SerializedName("class_name")
        public String className;
        @SerializedName("class_subtitle")
        public String classSubtitle;
        @SerializedName("icon")
        public String icon;
        @SerializedName("content")
        public String content;
        @SerializedName("isuse")
        public int isuse;
        @SerializedName("pay_type")
        public int payType;

        public boolean isChecked;
        @Override
        public String toString() {
            return "DataBean{" +
                    "className='" + className + '\'' +
                    ", classSubtitle='" + classSubtitle + '\'' +
                    ", icon='" + icon + '\'' +
                    ", content='" + content + '\'' +
                    ", isuse=" + isuse +
                    ", payType=" + payType +
                    '}';
        }
    }
}

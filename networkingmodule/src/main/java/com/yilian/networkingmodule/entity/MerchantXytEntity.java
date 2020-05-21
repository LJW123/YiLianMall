package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/2/5 0005.
 */

public class MerchantXytEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("year")
        public String year;
        @SerializedName("isCredit")  //是否开通 信用通 0未
        public String isCredit;
        @SerializedName("isMerchantCredit") //是否是商家版信用通 信用通 0未
        public String isMerchantCredit;
        @SerializedName("isServiceProvider") //是否是服务中心版信用通 信用通 0未
        public String isServiceProvider;
        @SerializedName("isTechnique")
        public String isTechnique;
        @SerializedName("merchantCredit")
        public ArrayList<String> merchantCredit;

        @SerializedName("banner")
        public ArrayList<BannerBean> banner;

        public static class BannerBean {
            @SerializedName("create_time")
            public String createTime;
            @SerializedName("id")
            public String id;
            @SerializedName("logo")
            public String logo;
            @SerializedName("order")
            public String order;
            @SerializedName("update_time")
            public String updateTime;
            @SerializedName("url_content")
            public String urlContent;
            @SerializedName("url_type")
            public int urlType;
        }

        @SerializedName("creditService")
        public ArrayList<ListBean> creditService;

        @SerializedName("shopTechService")
        public ArrayList<ListBean> shopTechService;

        public static class ListBean {
            @SerializedName("amount_pay")
            public String amountPay;
            @SerializedName("discount")
            public String discount;
            @SerializedName("duration")
            public String duration;
            @SerializedName("duration_name")
            public String durationName;
            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("original_cost")
            public String originalCost;
            @SerializedName("service_code")
            public String serviceCode;
            @SerializedName("shop_corporate_type")
            public String shopCorporateType;
            @SerializedName("shop_size_type")
            public String shopSizeType;
        }

        @SerializedName("privilege")
        public ArrayList<PrivilegeBean> privilege;

        public static class PrivilegeBean {
            @SerializedName("desc")
            public String desc;
            @SerializedName("id")
            public String id;
            @SerializedName("index")
            public String index;
            @SerializedName("link_content")
            public String linkContent;
            @SerializedName("link_title")
            public String linkTitle;
            @SerializedName("link_type")
            public int linkType;
            @SerializedName("logo")
            public String logo;
            @SerializedName("name")
            public String name;
        }

    }
}

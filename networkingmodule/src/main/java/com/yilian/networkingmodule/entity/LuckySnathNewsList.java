package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/22 0022.
 */

public class LuckySnathNewsList extends HttpResultBean {

    @SerializedName("head_photo")
    public String headPhoto;

    @SerializedName("data")
    public ArrayList<DataBean> data;

    public class DataBean {
        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("time")
        public String time;
        @SerializedName("snatch_index")
        public String snatchIndex;
        @SerializedName("award_linkman")
        public String awardLinkman;
        @SerializedName("express_time")
        public String expressTime;
        @SerializedName("award_city")
        public String awardCity;
        @SerializedName("photo")
        public String photo;
        @SerializedName("ip_address")
        public String ipAddress;
    }

}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/2/5 0005.
 */

public class MerchantXytTopEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("merchantCredit")
        public String merchantCredit;
        @SerializedName("statistics")
        public ArrayList<StatisticsBean> statistics;

        public static class StatisticsBean {
            @SerializedName("name")
            public String name;
            @SerializedName("num")
            public String num;
            @SerializedName("type")
            public String type;
        }
    }
}

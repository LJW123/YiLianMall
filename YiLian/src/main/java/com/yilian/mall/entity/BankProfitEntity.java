package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class BankProfitEntity extends BaseEntity{


    /**
     * income_lebi :
     * list : [{"income_time":"","consumer_gain_lebi":""},{}]
     */

    @SerializedName("income_lebi")
    public String totalProfit;
    /**
     * income_time :
     * consumer_gain_lebi :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        @SerializedName("income_time")
        public String profitTime;
        @SerializedName("consumer_gain_lebi")
        public String dailyProfit;

    }

}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class BankProfitRateEntity extends BaseEntity {

    /**
     * percentage :
     * msg :
     */

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        @SerializedName("percentage")
        public String percentage;
        @SerializedName("msg")
        public String msg;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by  on 2017/6/16 0016.
 */

public class MerchantDiscountChangeRecordEntity extends BaseEntity {

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * later : 0
         * time : 1497505756
         * ip : 127.0.0.1
         */

        @SerializedName("later")
        public float later;
        @SerializedName("time")
        public String time;
        @SerializedName("ip")
        public String ip;
    }
}

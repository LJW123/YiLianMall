package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/27 0027.
 */

public class JPSignBeforeEntity extends BaseEntity{


    /**
     * name :
     * level : 1
     * prize_money : [2,4,6,9,12,15,18,20]
     * cycle_count : 1
     * total_count : 1
     * is_sign : 1
     * total_coupons : 200
     * cycle_coupons : 200
     * end_date : 1477636581
     * daily_turntable_count : 0
     */

    @SerializedName("data")
    public DataBean data;

    @SerializedName("send_money")
    public List<Integer> sendMoney;

    public class DataBean {
        @SerializedName("name")
        public String name;
        @SerializedName("level")
        public int level;
        @SerializedName("cycle_count")
        public String cycleCount;
        @SerializedName("total_count")
        public String totalCount;
        @SerializedName("is_sign")
        public int isSign;
        @SerializedName("total_coupons")
        public String totalCoupons;
        @SerializedName("cycle_coupons")
        public String cycleCoupons;
        @SerializedName("end_date")
        public String endDate;
        @SerializedName("daily_turntable_count")
        public String dailyTurntableCount;
        @SerializedName("prize_money")
        public List<Integer> prizeMoney;
    }
}

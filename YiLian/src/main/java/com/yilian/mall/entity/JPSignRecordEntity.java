package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by  on 2016/10/22 0022.
 */

public class JPSignRecordEntity extends BaseEntity {


    /**
     * prize_money : [2,4,6,9,12,15,18,20]
     * cycle_count : 1
     * total_count : 1
     * is_sign : 1
     * total_coupons : 1
     * cycle_coupons : 1
     * end_date : 1
     * daily_turntable_count : 1
     */

    @SerializedName("data")
    public DataBean JPLocalHeaderData;

    public class DataBean {
        @SerializedName("cycle_count")
        public String JPLocalHeaderCycleCount;
        @SerializedName("total_count")
        public String JPLocalHeaderTotalCount;
        @SerializedName("is_sign")
        public String JPLocalHeaderIsSign;
        @SerializedName("total_coupons")
        public String JPLocalHeaderTotalCoupons;
        @SerializedName("cycle_coupons")
        public String JPLocalHeaderCycleCoupons;
        @SerializedName("end_date")
        public String JPLocalHeaderEndDate;
        @SerializedName("daily_turntable_count")
        public String JPLocalHeaderDailyTurntableCount;
        @SerializedName("prize_money")
        public List<Integer> JPLocalHeaderPrizeMoney;
    }
}

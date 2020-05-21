package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/8/19 0019.
 */

public class PerformanceEntity  extends HttpResultBean {


    @SerializedName("direct")
    public Direct direct;
    @SerializedName("all")
    public All all;

    public class Direct {
        @SerializedName("mer_deal")
        public String merDeal;
        @SerializedName("mer_bonus")
        public String merBonus;
        @SerializedName("user_deal")
        public String userDeal;
        @SerializedName("user_bonus")
        public String userBonus;
    }

    public class All {
        @SerializedName("user_count")
        public String userCount;
        @SerializedName("untity_count")
        public String untityCount;
        @SerializedName("entity_count")
        public String entityCount;
        @SerializedName("mer_deal")
        public String merDeal;
        @SerializedName("mer_bonus")
        public String merBonus;
        @SerializedName("user_deal")
        public String userDeal;
        @SerializedName("user_bonus")
        public String userBonus;
    }
}

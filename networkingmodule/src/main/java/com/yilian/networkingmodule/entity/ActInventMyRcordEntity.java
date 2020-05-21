package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActInventMyRcordEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("sub_member_count")
        public String sub_member_count;
        @SerializedName("my_rank")
        public float my_rank;
        @SerializedName("get_integral")
        public String get_integral;

    }

}

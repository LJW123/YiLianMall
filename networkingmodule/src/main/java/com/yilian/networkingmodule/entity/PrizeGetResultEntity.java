package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/4/29 0029.
 */
public class PrizeGetResultEntity extends BaseEntity{

    /**
     * data : {"order_index":"","order_id":"","done_time":""}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * order_index :
         * order_id :
         * done_time :
         */

        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("done_time")
        public String doneTime;
    }
}

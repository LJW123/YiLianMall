package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ASUS on 2017/12/5 0005.
 */

public class StealMoreTrends extends HttpResultBean {


    /**
     * code : 1
     * data : [{"merchant_id":"","to_name":"","from_name":"","apply_at":"","merchant_name":""},{}]
     */
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * merchant_id :
         * to_name :
         * from_name :
         * apply_at :
         * merchant_name :
         */
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("to_name")
        public String toName;
        @SerializedName("from_name")
        public String fromName;
        @SerializedName("apply_at")
        public String applyAt;
        @SerializedName("merchant_name")
        public String merchantName;
    }
}

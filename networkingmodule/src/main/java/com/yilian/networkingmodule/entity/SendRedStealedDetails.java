package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ASUS on 2017/12/6 0006.
 */

public class SendRedStealedDetails extends HttpResultBean {

    /**
     * code : 1
     * data : {"count":"","integral":"","list":[{"apply_at":" ","last_amount":"","status":""},{}]}
     */
    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * count :
         * integral :
         * list : [{"apply_at":" ","last_amount":"","status":""},{}]
         */
        @SerializedName("count")
        public String count;
        @SerializedName("integral")
        public String integral;
        @SerializedName("list")
        public java.util.List<Details> list;

        public static class Details {
            /**
             * apply_at :
             * last_amount :
             * status :
             */
            @SerializedName("apply_at")
            public String applyAt;
            @SerializedName("last_amount")
            public String lastAmount;
            @SerializedName("status")
            public String status;
        }
    }
}

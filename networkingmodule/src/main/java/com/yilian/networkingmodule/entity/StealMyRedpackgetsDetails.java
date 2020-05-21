package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ZYH on 2017/10/13.
 */

public class StealMyRedpackgetsDetails extends HttpResultBean {


    /**
     * code : 1
     * data : [{"name":"","apply_at":"","amount":"","photo":""},{}]
     */
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        /**
         * name :
         * apply_at :
         * amount :
         * photo :
         */
        @SerializedName("name")
        public String name;
        @SerializedName("apply_at")
        public String applyAt;
        @SerializedName("amount")
        public String amount;
        @SerializedName("photo")
        public String photo;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/10/11 0011.
 */

public class MerchantOrderNumber extends HttpResultBean {

    /**
     * data : {"ready":"5","send":"0","receive":"0","done":"9"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * ready : 5
         * send : 0
         * receive : 0
         * done : 9
         */

        @SerializedName("ready")
        public int ready;
        @SerializedName("send")
        public int send;
        @SerializedName("receive")
        public int receive;
        @SerializedName("done")
        public int done;
    }
}

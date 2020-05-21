package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by  on 2017/10/11 0011.
 */

public class MerchantAfterSaleOrderNumber extends HttpResultBean {


    /**
     * data : {"ready":0,"doing":7,"done":7}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * ready : 0
         * doing : 7
         * done : 7
         */

        @SerializedName("ready")
        public int ready;//待审核
        @SerializedName("doing")
        public int doing;//处理中
        @SerializedName("done")
        public int done;//已完成
    }
}

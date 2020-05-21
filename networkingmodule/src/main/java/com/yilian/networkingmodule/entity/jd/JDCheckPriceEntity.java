package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/5/29.
 */

public class JDCheckPriceEntity extends HttpResultBean {

    /**
     * data : {"marketPrice":3500,"price":3350,"skuId":7578335,"jdPrice":3350}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * marketPrice : 3500
         * price : 3350
         * skuId : 7578335
         * jdPrice : 3350
         */

        @SerializedName("marketPrice")
        public String marketPrice;
        @SerializedName("price")
        public String price;
        @SerializedName("skuId")
        public String skuId;
        @SerializedName("jdPrice")
        public String jdPrice;
    }
}

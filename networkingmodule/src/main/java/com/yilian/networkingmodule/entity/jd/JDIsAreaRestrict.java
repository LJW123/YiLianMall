package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/5/29.
 */

public class JDIsAreaRestrict extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * skuId : 1406293
         * isAreaRestrict : false
         */

        @SerializedName("skuId")
        public String skuId;
        /**
         * false 不限制
         * true 限制
         */
        @SerializedName("isAreaRestrict")
        public boolean isAreaRestrict;
    }
}

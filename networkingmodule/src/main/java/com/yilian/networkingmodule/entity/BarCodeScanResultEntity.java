package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;

/**
 * Created by  on 2017/8/30 0030.
 */

public class BarCodeScanResultEntity extends HttpResultBean {

    /**
     * data : {"goods_name":"订单的","goods_cost":"0","goods_code":"123123123123","goods_norms":"尺寸：L"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * goods_name : 订单的
         * goods_cost : 0
         * goods_code : 123123123123
         * goods_norms : 尺寸：L
         */

        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_code")
        public String goodsCode;
        @SerializedName("goods_norms")
        public String goodsNorms;
    }
}

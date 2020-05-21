package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 苏宁政企判断是否为厂送商品
 *
 * @author Created by Zg on 2018/7/25.
 */

public class SnFactorySendEntity extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        @SerializedName("skuId")
        public String skuId;
        /**
         * 是否厂送商品 01-是；02-否
         */
        @SerializedName("isFactorySend")
        public String isFactorySend;

        public String getSkuId() {
            return skuId == null ? "" : skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getIsFactorySend() {
            return isFactorySend == null ? "" : isFactorySend;
        }

        public void setIsFactorySend(String isFactorySend) {
            this.isFactorySend = isFactorySend;
        }
    }



}

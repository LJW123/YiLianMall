package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/8/29 0029.
 */

public class MerchantCartListEntity extends HttpResultBean {
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public class DataBean {
        @SerializedName("cart_index")
        public String cartIndex;
        @SerializedName("goods_index")
        public String goodsIndex;
        @SerializedName("goods_sku")
        public String goodsSku;
        @SerializedName("goods_count")
        public int goodsCount;
        @SerializedName("sku_code")
        public String skuCode;
        @SerializedName("merchant_uid")
        public String merchantUid;
        @SerializedName("pay_type")
        public String payType;
        @SerializedName("add_time")
        public String addTime;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_norms")
        public String goodsNorms;
        @SerializedName("goods_cost_price")
        public float goodsCostPrice;
        @SerializedName("merchant_integral")
        public float merchantIntegral;
        @SerializedName("user_integral")
        public float userIntegral;
        @SerializedName("goods_status")
        public String goodsStatus;
        @SerializedName("bonus")
        public float bonus;

        @Override
        public String toString() {
            return "ListBean{" +
                    "cartIndex='" + cartIndex + '\'' +
                    ", goodsIndex='" + goodsIndex + '\'' +
                    ", goodsSku='" + goodsSku + '\'' +
                    ", goodsCount=" + goodsCount +
                    ", skuCode='" + skuCode + '\'' +
                    ", merchantUid='" + merchantUid + '\'' +
                    ", payType='" + payType + '\'' +
                    ", addTime='" + addTime + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsNorms='" + goodsNorms + '\'' +
                    ", goodsCostPrice=" + goodsCostPrice +
                    ", merchantIntegral=" + merchantIntegral +
                    ", userIntegral=" + userIntegral +
                    ", goodsStatus='" + goodsStatus + '\'' +
                    ", bonus=" + bonus +
                    '}';
        }
    }
}

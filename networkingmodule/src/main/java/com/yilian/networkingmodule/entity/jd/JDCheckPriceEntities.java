package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/6/29.
 * 购物车列表提交订单前检查价格
 */

public class JDCheckPriceEntities extends HttpResultBean {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * cart_index : 1898 //购物车编号
         * sku : 2291963 //商品id
         * priceInfo : {"marketPrice":598,"price":70.03,"skuId":2291963,"jdPrice":129}
         */

        @SerializedName("cart_index")
        public String cartIndex;
        @SerializedName("sku")
        public String sku;
        @SerializedName("priceInfo")
        public PriceInfoBean priceInfo;

        public static class PriceInfoBean {
            /**
             * marketPrice : 598 //市场价
             * price : 70.03 //京东扣除我们账户价格 最低价
             * skuId : 2291963
             * jdPrice : 129 //售卖价
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
}

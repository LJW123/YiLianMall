package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray_L_Pain on 2017/10/11 0011.
 */

public class MerchantManagerDetailEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("goods_index")
        public String goods_index;
        @SerializedName("goods_name")
        public String goods_name;
        @SerializedName("goods_icon")
        public String goods_icon;
        @SerializedName("goods_sale")
        public String goods_sale;
        @SerializedName("goods_sku_count")
        public String goods_sku_count;

        @Override
        public String toString() {
            return "ListBean{" +
                    "goods_index='" + goods_index + '\'' +
                    ", goods_name='" + goods_name + '\'' +
                    ", goods_icon='" + goods_icon + '\'' +
                    ", goods_sale='" + goods_sale + '\'' +
                    ", goods_sku_count='" + goods_sku_count + '\'' +
                    ", is_normal='" + is_normal + '\'' +
                    ", is_yhs='" + is_yhs + '\'' +
                    ", is_jfg='" + is_jfg + '\'' +
                    ", goods_price='" + goods_price + '\'' +
                    ", goods_cost='" + goods_cost + '\'' +
                    ", retail_price='" + retail_price + '\'' +
                    ", goods_sku='" + goods_sku + '\'' +
                    ", goods_integral='" + goods_integral + '\'' +
                    ", goodsSkuPrice=" + goodsSkuPrice +
                    ", goodsSkuProperty=" + goodsSkuProperty +
                    ", goodsSkuValues=" + goodsSkuValues +
                    '}';
        }

        @SerializedName("is_normal")
        public String is_normal;
        @SerializedName("is_yhs")
        public String is_yhs;
        @SerializedName("is_jfg")
        public String is_jfg;
        @SerializedName("goods_price")
        public String goods_price;
        @SerializedName("goods_cost")
        public String goods_cost;
        @SerializedName("retail_price")
        public String retail_price;
        @SerializedName("goods_sku")
        public String goods_sku;
        @SerializedName("goods_integral")
        public String goods_integral;
        @SerializedName("goods_album")
        public ArrayList<String> goods_album;
        @SerializedName("authority")
        public String authority;


        @SerializedName("goods_sku_price")
        public List<GoodsSkuPriceBean> goodsSkuPrice;
        @SerializedName("goods_sku_property")
        public List<GoodsSkuPropertyBean> goodsSkuProperty;
        @SerializedName("goods_sku_values")
        public List<GoodsSkuPropertyBean> goodsSkuValues;
    }
}

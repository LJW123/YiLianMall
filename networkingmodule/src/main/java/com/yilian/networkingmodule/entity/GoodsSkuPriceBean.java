package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/10/11 0011.
 */

public class GoodsSkuPriceBean {
    @SerializedName("sku_goods")
    public String skuGoods;
    @SerializedName("sku_info")
    public String skuInfo;
    @SerializedName("sku_market_price") //市场价
    public String skuMrketPrice;
    @SerializedName("sku_integral_price") //yhs价
    public String skuIntegralPrice;
    @SerializedName("sku_integral_money")
    public String skuIntegralMoney;
    @SerializedName("sku_inventory") //可订货库存
    public String skuInventory;
    @SerializedName("sku_cost_price") //用户支付价
    public String skuCostPrice;
    @SerializedName("sku_retail_price") //供货商结算价
    public String skuRetailPrice;
    @SerializedName("change_time")
    public String changeTime;
    @SerializedName("supplier_retail_price")
    public String supplierRetailPrice;
    @SerializedName("return_integral") //赠送用户奖券
    public String returnIntegral;
    @SerializedName("return_merchant_integral") //商家销售奖券
    public String returnMerchantIntegral;
    @SerializedName("sku_repertory") //实际库存
    public String skuRepertory;

    @Override
    public String toString() {
        return "GoodsSkuPriceBean{" +
                "skuGoods='" + skuGoods + '\'' +
                ", skuInfo='" + skuInfo + '\'' +
                ", skuMrketPrice='" + skuMrketPrice + '\'' +
                ", skuIntegralPrice='" + skuIntegralPrice + '\'' +
                ", skuIntegralMoney='" + skuIntegralMoney + '\'' +
                ", skuInventory='" + skuInventory + '\'' +
                ", skuCostPrice='" + skuCostPrice + '\'' +
                ", skuRetailPrice='" + skuRetailPrice + '\'' +
                ", changeTime='" + changeTime + '\'' +
                ", supplierRetailPrice='" + supplierRetailPrice + '\'' +
                ", returnIntegral='" + returnIntegral + '\'' +
                ", returnMerchantIntegral='" + returnMerchantIntegral + '\'' +
                ", skuRepertory='" + skuRepertory + '\'' +
                '}';
    }
}

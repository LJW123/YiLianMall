package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * sku规格属性名/值
 *
 */
public class GoodsSkuPrice implements Serializable{
	@SerializedName("return_integral")
	public String returnIntegral;//消费返利
	@SerializedName("return_bean")
	public String returnBean;
	@SerializedName("change_time")
	public String changeTime;

	/**
	 * 商品ID
	 */
	@SerializedName("sku_goods")
	public String goodId;
	
	/**
	 * 商品sku
	 */
	@SerializedName("sku_info")
	public String skuInfo;
	
	/**
	 * 商品兑换奖券价
	 */
	@SerializedName("sku_integral_price")
	public String skuIntegralPrice;
	
	/**
	 * 乐选区额外支付金额(单位：分)
	 */
	@SerializedName("sku_integral_money")
	public String skuIntegralMoney;

	/**
	 * cost
	 */
	@SerializedName("sku_cost_price")
	public String skuCostPrice;

	/**
	 * 库存
	 */
	@SerializedName("sku_inventory")
	public String skuInventory;

	/**
	 * 市场价(单位：分)
	 */
	@SerializedName("sku_market_price")
	public String skuMarketPrice;

	/**
	 * 奖券购商品 现金价格
	 */
	@SerializedName("sku_retail_price")
	public String skuRetailPrice;


	@Override
	public String toString() {
		return "GoodsSkuPrice{" +
				"goodId='" + goodId + '\'' +
				", skuInfo='" + skuInfo + '\'' +
				", skuIntegralPrice='" + skuIntegralPrice + '\'' +
				", skuIntegralMoney='" + skuIntegralMoney + '\'' +
				", skuCostPrice='" + skuCostPrice + '\'' +
				", skuMarketPrice='" + skuMarketPrice + '\'' +
				'}';
	}
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class GoodsSkuValues {
	
	/**
	 * sku属性值归属的sku属性ID
	 */
	@SerializedName("sku_parent_id")
	public String skuParentId;
	
	/**
	 * 商品属性id
	 */
	@SerializedName("sku_id")
	public String skuId;
	
	/**
	 * sku属性名字
	 */
	@SerializedName("sku_name")
	public String sku_name;

}

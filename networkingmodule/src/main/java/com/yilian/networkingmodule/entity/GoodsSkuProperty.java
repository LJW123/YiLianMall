package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * sku规格属性名/值
 *
 */
public class GoodsSkuProperty implements Serializable{
	
	/**
	 * 商品属性/属性值id
	 */
	@SerializedName("sku_index")
	public String skuId;
	
	/**
	 * sku属性(值)名字
	 */
	@SerializedName("sku_name")
	public String skuName;

	/**
	 * sku属性值归属的sku属性ID,只有规格属性值中有此字段
	 */
	@SerializedName("sku_parent")
	public String skuParentId;

	@Override
	public String toString() {
		return "GoodsSkuProperty{" +
				"skuId='" + skuId + '\'' +
				", skuName='" + skuName + '\'' +
				", skuParentId='" + skuParentId + '\'' +
				'}';
	}
}

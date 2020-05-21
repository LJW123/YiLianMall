package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class GoodsCollectListInfor {
	/**
	 * 收藏记录编号
	 */
	@SerializedName("collect_index")
	public String collectIndex;
	

	/**
	 * 收藏商品ID
	 */
	@SerializedName("collect_goods_id")
	public String collectGoodsId;

	@SerializedName("filiale_id")
	public String filialeId;

	/**
	 * 商品分区
	 */
	@SerializedName("collect_type")
	public String collectType;

	/**
	 * 收藏商品名字
	 */
	@SerializedName("collect_goods_name")
	public String collectGoodsName;
	

	/**
	 * 收藏商品路径
	 */
	@SerializedName("collect_goods_icon")
	public String collectGoodsIcon;
	

	/**
	 * 收藏时间
	 */
	@SerializedName("collect_time")
	public String collectTime;
	

	/**
	 * 商品奖券价格
	 */
	@SerializedName("collect_goods_integral")
	public String collectGoodsIntegral;
}

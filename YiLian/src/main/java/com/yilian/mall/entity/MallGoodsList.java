package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class MallGoodsList extends BaseEntity{

	/**
	 * 商品ID
	 */
	@SerializedName("goods_id")
	public String goodsId;
	
	/**
	 * 商品名称
	 */
	@SerializedName("goods_name")
	public String goodsName;
	
	/**
	 * 商品销量
	 */
	@SerializedName("goods_sale")
	public String goodsSalesVolume;
	
	/**
	 * 商品兑换奖券价
	 */
	@SerializedName("goods_integral_price")
	public String goodsIntegralPrice;
	
	/**
	 * 商品上架时间
	 */
	@SerializedName("goods_online")
	public String goodsOnlineTime;
	
	/**
	 * 商品评分
	 */
	@SerializedName("goods_grade")
	public String goodsGrade;
	
	/**
	 * 商品icon地址
	 */
	@SerializedName("goods_icon")
	public String goods_icon;
	
	/**
	 * 好评百分比
	 */
	@SerializedName("goods_good")
	private String goods_good;
	
	/**
	 * 商品浏览次数
	 */
	@SerializedName("goods_renqi")
	public String goodsViewedCount;
	
	
	/**
	 * 乐选区额外支付金额(单位：分)
	 */
	@SerializedName("goods_integral_money")
	public String integralMoney;
	
	/**
	 * 市场价(单位：分)
	 */
	@SerializedName("goods_market_price")
	public String marketPrice;
	
	/**
	 * 商品分区（1乐换、2乐选、3乐购）
	 */
	@SerializedName("goods_type")
	public int goodType;

}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class Merchant extends BaseEntity{
	
	/**
	 * 商家类型 1代表联盟商家 2代表兑换中心
	 */
	@SerializedName("merchant_type")
	public int merchantType;
	
	/**
	 * merchantType == 1时 类型为 MerchantDetails
	 * merchantType == 2时 类型为 FilialeDetails
	 * 
	 */
	public MerchInfo info;

	/**
	 * 点赞数量
	 * 0 未点赞 ， 1 已点赞
	 */
	@SerializedName("is_praise")
	public int isPraise;
	
}

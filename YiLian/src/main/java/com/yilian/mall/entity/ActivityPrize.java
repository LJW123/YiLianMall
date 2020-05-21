package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class ActivityPrize {

	@SerializedName("prize_type")
	public String prizeType;
	
	@SerializedName("prize_goods_name")
	public String prizeName;
	
	@SerializedName("prize_goods_url")
	public String prizeImgUrl;
	
	/**
	 * 奖品等级（0，1，2代表1-3等奖）
	 */
	@SerializedName("prize_level")
	public int prizeLevel;
	
	/**
	 * 奖品总数量
	 */
	@SerializedName("prize_amount")
	public int prizeAmount;
}

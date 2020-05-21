package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class ShakeWinners {
	
	/**
	 * 归属活动唯一 id
	 */
	@SerializedName("voucher_for_activity")
	public String voucherForActivity;
	
	/**
	 * 奖品名字
	 */
	@SerializedName("voucher_prize_name")
	public String voucherPrizeName;
	
	/**
	 * 中奖用户 手机号
	 */
	@SerializedName("user_phone")
	public String userPhone;

}

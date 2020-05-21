package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class PrizeVoucher {

	/**
	 * 奖品图片
	 */
	@SerializedName("img_url")
	public String imgUrl;
	
	/**
	 * 奖品名字
	 */
	@SerializedName("voucher_prize_name")
	public String prizeName;
	
	/**
	 * 活动名字
	 */
	@SerializedName("activity_name")
	public String activityName;
	
	/**
	 * 过期时间
	 */
	@SerializedName("voucher_valid_time")
	public long validTime;
	
	/**
	 * 凭证状态(0未使用，1还需要）
	 */
	@SerializedName("voucher_status")
	public int status;
	
	/**
	 * 凭证编号
	 */
	@SerializedName("voucher_index")
	public String id;

//"img_url":"",//奖品图片
//"voucher_prize_name":"",//奖品名字
//"activity_name":"",//活动名字
//"voucher_valid_time":"",//过期时间
//"voucher_status":"",//凭证状态(0未使用，1还需要）
//"voucher_id":""凭证编号
}

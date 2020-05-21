/**
 * 
 */
package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Administrator
 * 大家猜活动中奖用户
 */
public class GuessWinnerInfo extends BaseEntity {
	
	/**
	 * 用户猜的数字
	 */
	public String figure;

	/**
	 * 中奖用户手机号
	 */
	@SerializedName("user_phone")
	public String userPhone;
	
	/**
	 * 奖品名称
	 */
	@SerializedName("prize_name")
	public String prizeName;
	
	/**
	 * 奖品图片地址
	 */
	@SerializedName("prize_image")
	public String prizeImgUrl;
	
	/**
	 * 中奖时间
	 */
	@SerializedName("prize_time")
	public long prizeTime;
}

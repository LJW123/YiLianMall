package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 活动中奖用户信息
 * @author Administrator
 *
 */
public class PrizeWinnerInfo {

	/**
	 * 归属活动唯一id(摇一摇活动)
	 */
	@SerializedName("voucher_for_activity")
	private String activityIndex;
	
	/**
	 * 摇一摇奖品名字
	 */
	@SerializedName("voucher_prize_name")
	private String shakePrizeName;
	
	/**
	 * 摇一摇中奖时间
	 */
	@SerializedName("voucher_grant_time")
	private long shakePrizeTime=0;
	
	/**
	 * 摇一摇奖品图片地址
	 */
	@SerializedName("img_url")
	private String shakePrizeImgUrl;
	
	/**
	 * 中奖用户手机号
	 */
	@SerializedName("user_phone")
	private String userPhone;
	
	/**
	 * 奖品名称
	 */
	@SerializedName("prize_name")
	private String prizeName;
	
	/**
	 * 奖品图片地址
	 */
	@SerializedName("prize_image")
	private String prizeImgUrl;
	
	/**
	 * 中奖时间
	 */
	@SerializedName("prize_time")
	private long prizeTime=0;

	public String getActivityIndex() {
		return activityIndex;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public String getPrizeName() {
		return prizeName==null?shakePrizeName:prizeName;
	}

	public String getPrizeImgUrl() {
		return prizeImgUrl==null?shakePrizeImgUrl:prizeImgUrl;
	}

	public long getPrizeTime() {
		return prizeTime==0?shakePrizeTime:prizeTime;
	}
	
}

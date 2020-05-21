package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class ShakeResult extends BaseEntity{

//	/**
//	 * 服务器处理结果 -17 1 -3 0 -4 -5
//	 */
//	public int code;
	/**
	 * 活动id
	 */
	@SerializedName("activity_index")
	public String acticityIndex;
	
	/**
	 * 0表示用户还没中过奖，1表示用户已经中过奖，2表示奖品已发完了
	 */
	public String status;
	
	/**
	 * 获奖商品名字，未获奖时默认为空
	 */
	@SerializedName("prize_name")
	public String prizeName;
	
	/**
	 * 实物奖凭凭证编号，未获奖用0表示(客户端以此值判断是否获奖)
	 */
	@SerializedName("prize_voucher")
	public int prizeVoucher;
	
	/**
	 * 奖品图片地址
	 */
	@SerializedName("prize_img_url")
	public String prizeImgUrl;
	
	/**
	 * 凭证有效期
	 */
	@SerializedName("voucher_valid_time")
	public String voucherValidTime;
}

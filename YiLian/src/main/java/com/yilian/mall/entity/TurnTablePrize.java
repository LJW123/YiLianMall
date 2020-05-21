package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 参加转盘活动抽奖结果
 * @author Administrator
 *
 */
public class TurnTablePrize extends BaseEntity{
//	/**
//	 * 服务器处理结果(1/0/-3/-4/-5/-11/-13/-17/-18/-22)
//	 */
//	public int code;
	
	/**
	 * 活动id
	 */
	@SerializedName("activity_index")
	public String activityIndex;
	
	/**
	 * 用户剩余乐分数量
	 */
	public int integral;
	
	/**
	 * 获奖位置（0-5表示一到六等奖）
	 */
	@SerializedName("prize_levle")
	public int prizeLevel;
	
	/**
	 * 奖品类型，0表示虚拟，1表示实物
	 */
	@SerializedName("prize_type")
	public int prizeType;
	
	/**
	 * 获奖商品名字
	 */
	@SerializedName("prize_name")
	public String prizeName;
	
	/**
	 * 实物奖凭凭证编号
	 */
	@SerializedName("prize_voucher")
	public String prizeVoucher;
	
	/**
	 * 此用户已参与次数
	 */
	@SerializedName("truntable_count")
	public int turnTableCount;
}

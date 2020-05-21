package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class UserBaseData extends BaseEntity {
	
	/**
     * 用户当前乐发奖励包
     */
    @SerializedName("balance")
	public int balance;
	
	/**
     * 乐币奖励
     */
    @SerializedName("lebi")
	public float lebi;
	
	/**
	 * 乐分获得记录数量
	 */
	@SerializedName("lefen_gain_count")
	public String lefenGainCount;
	
	/**
	 * 乐分兑换记录数量
	 */
	@SerializedName("lefen_expend_count")
	public String lefenExpendCount;
	
	/**
	 * 活动参与记录数量
	 */
	@SerializedName("activity_play_count")
	public String activityPlayCount;
	
	/**
	 * 中奖凭证数量
	 */
	@SerializedName("award_voucher_count")
	public String awardVoucherCount;
	
	/**
	 * 商品收藏数量
	 */
	@SerializedName("goods_collect_count")
	public String goodsCollectCount;
	
	/**
	 * 乐购区现金消费记录数量
	 */
	@SerializedName("legou_count")
	public String legouCount;
	
	/**
	 * 乐币消费记录数量
	 */
	@SerializedName("lebi_expend_count")
	public String lebiExpendCount;

}

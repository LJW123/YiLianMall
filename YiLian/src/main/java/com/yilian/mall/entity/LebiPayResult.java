package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class LebiPayResult extends BaseEntity {

	/**
     * APP用户乐币奖励
     */
    public String lebi;
	
	/**
	 * 交易流水号
	 */
	@SerializedName("deal_id")
	public String dealId;
	
	/**
	 * 交易时间
	 */
	@SerializedName("deal_time")
	public String dealTime;
}

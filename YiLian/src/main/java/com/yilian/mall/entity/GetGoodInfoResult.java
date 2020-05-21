package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class GetGoodInfoResult extends BaseEntity {
	/**
	* 用户登录状态是否有效，0或1
	*/
	@SerializedName("login_status")
	public int loginState;
	
	/**
	 * 是否已经收藏商品，0或1，未登录时返回0
	 */
	@SerializedName("yet_collect")
	public int yetCollect;

	/**
	 * 商品信息
	 */
	@SerializedName("good")
	public MallGoodsInfo good;
}

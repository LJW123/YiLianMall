package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Nearby extends BaseEntity {
	
	/**
	 * 兑换中心列表
	 */
	@SerializedName("filiale_list")
	public ArrayList<FilialeList> filialeList;
	
	/**
	 * 联盟商家列表
	 */
	@SerializedName("merchant_list")
	public ArrayList<MerchantList> merchantList;
	
}

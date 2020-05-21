package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPrizeVoucherListResult extends BaseEntity{
	/**
	 * 凭证列表
	 */
	@SerializedName("list")
	public List<PrizeVoucher> vouchers;
}

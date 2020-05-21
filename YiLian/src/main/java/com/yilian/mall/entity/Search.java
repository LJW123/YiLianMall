package com.yilian.mall.entity;

import java.util.ArrayList;
import java.util.List;

public class Search extends BaseEntity {

	public ArrayList<MerchantList> merchant_list;

	public List<MerchantList> getMerchant_list() {
		return merchant_list;
	}
	public void setMerchant_list(ArrayList<MerchantList> merchant_list) {
		this.merchant_list = merchant_list;
	}
}

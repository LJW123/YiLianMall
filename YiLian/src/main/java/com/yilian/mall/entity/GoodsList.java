package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodsList extends BaseEntity{
	
	/**
	 * 商品列表
	 */
	@SerializedName("list")
	public ArrayList<MallGoodsList> list;

}

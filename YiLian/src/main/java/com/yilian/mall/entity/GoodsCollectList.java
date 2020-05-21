package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodsCollectList extends BaseEntity{
	
	@SerializedName("list")
	public ArrayList<GoodsCollectListInfor> infor;
	
}

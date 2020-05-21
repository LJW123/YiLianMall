package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FqaActivityArrayList extends BaseEntity {
	
	
	@SerializedName("list")
	public ArrayList<FqaActivityList> infor;
	
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class RegisterDevice extends BaseEntity{
	
	/**
	 * 设备注册id
	 */
	@SerializedName("device_index")
	public String deviceIndex; // 返回设备编号

}

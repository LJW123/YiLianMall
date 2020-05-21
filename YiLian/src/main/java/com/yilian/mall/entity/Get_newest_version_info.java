package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class Get_newest_version_info extends BaseEntity {
	@SerializedName("android_version")
	public String androidVersion;
	private String info;

	private String android_type;

	private String force_version;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}

	public String getAndroid_type() {
		return android_type;
	}

	public void setAndroid_type(String android_type) {
		this.android_type = android_type;
	}

	public String getForce_version() {
		return force_version;
	}

	public void setForce_version(String force_version) {
		this.force_version = force_version;
	}
}

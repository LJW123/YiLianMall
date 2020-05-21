package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class UploadImageData extends BaseEntity{

	@SerializedName("url")
	public String url;
	@SerializedName("data")
	public String data;

}

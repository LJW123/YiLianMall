package com.yilian.mall.entity;

public class Upload_image_data extends BaseEntity{
	
	private String url;

	public String getFilename() {
		return url;
	}

	public void setFilename(String url) {
		this.url = url;
	}

}

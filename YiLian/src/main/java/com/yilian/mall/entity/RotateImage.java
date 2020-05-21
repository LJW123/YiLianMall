package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 首页轮播图item
 * @author Administrator
 *
 */
public class RotateImage {

	/**
	 * 类型，0网页连接，1商品详情页，2商品分类列表页，11/12/13/14代表4种活动参与页面
	 */
	@SerializedName("banner_type")
	public int type;
	
	/**
	 * 图片地址
	 */
	@SerializedName("image_url")
	public String imageUrl;
	
	/**
	 * 数据，对应type需要的数据
	 */
	@SerializedName("banner_data")
	public String data;


	public String titles;
}

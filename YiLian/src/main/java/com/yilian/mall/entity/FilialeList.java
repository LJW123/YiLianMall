package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;


/**
 * 兑换中心
 * @author lauyk
 *
 */
public class FilialeList {
	/**
	 * 兑换中心分店唯一ID
	 */
	@SerializedName("shop_index")
	public String shopIndex; 
	
	/**
	 * 兑换中心唯一ID
	 */
	@SerializedName("shop_filiale_id")
	public String shopFilialeId;
	
	/**
	 * 兑换中心门店标题
	 */
	@SerializedName("shop_name")
	public String shopName; 
	
	/**
	 * 兑换中心门店评分10-50之间，代表1-5颗星
	 */
	@SerializedName("graded")
	public String graded; 
	
	/**
	 * 省
	 */
	@SerializedName("shop_province")
	public String shopProvince; 
	
	/**
	 * 市
	 */
	@SerializedName("shop_city")
	public String shopCity; 
	
	/**
	 * 县
	 */
	@SerializedName("shop_county")
	public String shopCounty; 
	
	/**
	 * 兑换中心门店地址
	 */
	@SerializedName("shop_address")
	public String shopAddress;
	
	/**
	 * 兑换中心门店地理位置经度
	 */
	@SerializedName("shop_longitude")
	public String shopLongitude; //兑换中心门店地理位置经度
	
	/**
	 * 兑换中心门店地理位置纬度
	 */
	@SerializedName("shop_latitude")
	public String shopLatitude; //兑换中心门店地理位置纬度
	
	/**
	 * 联系电话
	 */
	@SerializedName("shop_tel")
	public String shopTel; //联系电话
	
	/**
	 * 兑换中心门店简介
	 */
	@SerializedName("shop_desp")
	public String shopDesp; //兑换中心门店简介
	
	/**
	 * 0分店，1总店
	 */
	@SerializedName("shop_type")
	public String shopType; //0分店，1总店
	
	/**
	 * 营业时间
	 */
	@SerializedName("shop_worktime")
	public String shopWorktime; //营业时间
	
	/**
	 * 兑换中心门店商家图片
	 */
	@SerializedName("shop_image")
	public String shopImage;//兑换中心门店商家图片
	
	/**
	 * 与当前位置的距离,km
	 */
	public String distance;
}

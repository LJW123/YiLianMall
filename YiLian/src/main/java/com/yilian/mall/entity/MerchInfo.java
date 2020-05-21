package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MerchInfo {
	
	/**
	 * 商家唯一id
	 */
	@SerializedName("merchant_id")
	public String merchantId;

	@SerializedName("merchant_type")
	public int merchantType;
	
	/**
	 * 商家名字标题
	 */
	@SerializedName("merchant_name")
	public String merchantName; 
	
	/**
	 * 商家评分10-50之间，代表1-5颗星
	 */
	@SerializedName("graded")
	public String graded; //
	
	/**
	 * 省
	 */
	@SerializedName("merchant_province")
	public String merchantProvince; 
	
	/**
	 * 市
	 */
	@SerializedName("merchant_city")
	public String merchantCity; 
	
	/**
	 * 县
	 */
	@SerializedName("merchant_county")
	public String merchantCounty; //县
	
	/**
	 * 商家地址
	 */
	@SerializedName("merchant_address")
	public String merchantAddress; 
	
	/**
	 * 商家地理位置经度
	 */
	@SerializedName("merchant_longitude")
	public String merchantLongitude; 
	
	/**
	 * 商家地理位置纬度
	 */
	@SerializedName("merchant_latitude")
	public String merchantLatitude; 
	
	/**
	 * 商家所属的二级行业
	 */
	@SerializedName("merchant_industry")
	public String merchantIndustry; 
	
	/**
	 * 商家所属的顶级行业
	 */
	@SerializedName("merchant_industry_parent")
	public String merchantIndustryParent; 
	
	/**
	 * 经营范围
	 */
	@SerializedName("merchant_scope")
	public String merchantScope; 
	
	/**
	 * 特色简介
	 */
	@SerializedName("merchant_desp")
	public String merchantDesp; 
	
	/**
	 * 营业时间
	 */
	@SerializedName("merchant_worktime")
	public String merchantWorktime; 
	
	/**
	 * 注册时间
	 */
	@SerializedName("merchant_regtime")
	public String merchantRegtime; 
	
	/**
	 * 商家环境图片
	 */
	public ArrayList<FilialeCodeImage> images;
	
	/**
	 * 商家送奖券比例，取值范围10-100，代表百分比
	 */
	@SerializedName("merchant_percent")
	public String merchantPercent;
	
	/**
	 * 商家固定电话号码
	 */
	@SerializedName("merchant_tel")
	public String merchantTel;
	
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
	public String shopLongitude; 
	
	/**
	 * 兑换中心门店地理位置纬度
	 */
	@SerializedName("shop_latitude")
	public String shopLatitude; 
	
	/**
	 * 联系电话
	 */
	@SerializedName("shop_tel")
	public String shopTel; 
	
	/**
	 * 兑换中心门店简介
	 */
	@SerializedName("shop_desp")
	public String shopDesp; 
	
	/**
	 * //0分店，1总店
	 */
	@SerializedName("shop_type")
	public String shopType; //0分店，1总店
	
	/**
	 * 营业时间
	 */
	@SerializedName("shop_worktime")
	public String shopWorktime;

	/**
	 * 是否支持乐币支付，0表示不支持，大于0表示支持
	 */
	@SerializedName("merchant_lebi_percent")
	public int merchantLebiPercent;

	/**
	 * 点赞数量
	 */
	@SerializedName("praise_count")
	public String praiseCount;

	/**
	 * 累计消费人次
	 */
	@SerializedName("deal_count")
	public String dealCount;

	/**
	 * 累计赠送数量
	 */
	@SerializedName("send_lefen_count")
	public String sendLefenCount;

	/**
	 * 人气
	 */
	@SerializedName("renqi")
	public String renqi;

}

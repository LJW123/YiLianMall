package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.mylibrary.entity.LocationEntity;

/**
 * 商家
 */
public class MerchantList  extends LocationEntity {
    @SerializedName("merchant_sort_time")
    public long merchantSortTime;
    /**
     * 商家唯一id
     */
    @SerializedName("merchant_id")
    public String merchantId;

	/**
	 * 送券类型
	 * 0 乐分币 1 抵扣券
	 */
	@SerializedName("merchant_type")
	public int merchantType;
	/**
	 * merchant_name
	 */
	@SerializedName("merchant_name")
    public String merchantName; 
	
	/**
	 * 商家评分10-50之间，代表1-5颗星
	 */
	@SerializedName("graded")
    public String graded; 
	
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
    public String merchantCounty; 
	
	/**
	 * 商家地址
	 */
	@SerializedName("merchant_address")
    public String merchantAddress; 
//
//	/**
//	 * 商家地理位置经度
//	 */
//	@SerializedName("merchant_longitude")
//    public String merchantLongitude;

//	/**
//	 * 商家地理位置纬度
//	 */
//	@SerializedName("merchant_latitude")
//    public String merchantLatitude;

	/**
	 * 商家所属的二级行业
	 */
	@SerializedName("merchant_industry")
    public String merchantIndustry; //商家所属的二级行业
	
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
	 * merchant_tel
	 */
	@SerializedName("merchant_tel")
    public String merchantTel; 
	
	/**
	 * 联盟商家图片
	 */
	@SerializedName("merchant_image")
    public String merchantImage; 
	
	/**
	 * 奖券赠送百分比
	 */
	@SerializedName("merchant_percent")
    public String merchantPercent;
	
//	/**
//	 * 与当前位置的距离,km
//	 */
//	public String distance;

	/**
	 * 点赞数量
	 */
	@SerializedName("praise_count")
	public String praiseCount;

	/**
	 * 是否支持配送
	 */
	@SerializedName("is_delivery")
	public String isDelivery;
	/**
	 * 是否支持预定
	 */
	@SerializedName("is_reserve")
	public String isReserve;
	/**
	 * 商家光临次数
	 */
	@SerializedName("renqi")
	public String renQi;
    @SerializedName("industry_name")
    public String industryName;

	/**
	 * 商家让利的百分比
	 */
	@SerializedName("mer_discount")
	public String merDiscount;



    @Override
    public String toString() {
        return "MerchantList{" +
                "merchantId='" + merchantId + '\'' +
                ", merchantType=" + merchantType +
                ", merchantName='" + merchantName + '\'' +
                ", graded='" + graded + '\'' +
                ", merchantProvince='" + merchantProvince + '\'' +
                ", merchantCity='" + merchantCity + '\'' +
                ", merchantCounty='" + merchantCounty + '\'' +
                ", merchantAddress='" + merchantAddress + '\'' +
                ", merchantLongitude='" + merchantLongitude + '\'' +
                ", merchantLatitude='" + merchantLatitude + '\'' +
                ", merchantIndustry='" + merchantIndustry + '\'' +
                ", merchantIndustryParent='" + merchantIndustryParent + '\'' +
                ", merchantScope='" + merchantScope + '\'' +
                ", merchantDesp='" + merchantDesp + '\'' +
                ", merchantWorktime='" + merchantWorktime + '\'' +
                ", merchantTel='" + merchantTel + '\'' +
                ", merchantImage='" + merchantImage + '\'' +
                ", merchantPercent='" + merchantPercent + '\'' +
                ", distance='" + distance + '\'' +
                ", praiseCount='" + praiseCount + '\'' +
                ", isDelivery='" + isDelivery + '\'' +
                ", isReserve='" + isReserve + '\'' +
                ", renQi='" + renQi + '\'' +
                ", industryName='" + industryName + '\'' +
                '}';
    }
}

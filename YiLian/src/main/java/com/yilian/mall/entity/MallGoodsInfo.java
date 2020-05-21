package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MallGoodsInfo{
	@SerializedName("return_integral")
	public String returnIntegral;
	/**
	 * 商品 ID
	 */
	@SerializedName("goods_id")
	public String goodsId;
	
	/**
	 * 商品名字
	 */
	@SerializedName("goods_name")
	public String goodsName;

	/**
	 * 商品品牌
	 */
	@SerializedName("goods_brand")
	public String goodsBrand;

	@SerializedName("goods_discount")
	public String goodDiscount; //10或20 代表服务费百分比

	@SerializedName("goods_video")
	public String goodsVideo; //商品视频
	
	/**
	 * 3乐购区（送区），4买区
	 */
	@SerializedName("goods_type")
	public int goodsType;

	/**
	 * 商品价格
	 */
	@SerializedName("goods_price")
	public String goodsPrice;

	/**
	 * 商品销量
	 */
	@SerializedName("goods_sale")
	public String goodsSalesVolume;

	/**
	 * 商品评分
	 */
	@SerializedName("goods_grade")
	public float goodsGrade;

	/**
	 * 商品评价数量
	 */
	@SerializedName("goods_grade_count")
	public int goodsGradeCount;

	/**
	 * 商品浏览次数
	 */
	@SerializedName("goods_renqi")
	public String goodsRenqi;

	/**
	 * 商品相册,加cdn前缀
	 */
	@SerializedName("goods_album")
	public List<String> goodsAlbum;

	/**
	 * 商品介绍
	 */
	@SerializedName("goods_introduce")
	public List<String> goodsIntroduce;

	/**
	 * 商品兑换奖券价
	 */
	@SerializedName("goods_integral_price")
	public String goodsIntegralPrice;
	
	/**
	 * 乐选区额外支付金额(单位：分)
	 */
	@SerializedName("goods_integral_money")
	public String goodsIntegralMoney;
	
	/**
	 * 市场价(单位：分)
	 */
	@SerializedName("goods_market_price")
	public String goodsMarketPrice;

	/**
	 * 好评率
	 */
	@SerializedName("goods_good")
	public String goodsGood;
	
	/**
	 * sku规格属性名列表，空数组表示没有sku表
	 */
	public List<GoodsSkuProperty> goods_sku_property;
	
	/**
	 * sku规格属性值列表,没有sku时为空数组
	 */
	public List<GoodsSkuProperty> goods_sku_values;
	
	/**
	 * sku规格价格列表,无sku表时使用0:0获取价格
	 */
	public List<GoodsSkuPrice> goods_sku_price;

	@SerializedName("goods_region")//商品地区（总部为0，其他未地区id）
	public String goods_region;
	@SerializedName("goods_supplier")//商品归属旗舰店（供货商商品归属id,其他商品为0）
	public String goods_supplier;
	@SerializedName("goods_filiale")//商品归属兑换中心（兑换中心商品归属id,其他商品为0）
	public String goods_filiale;
	@SerializedName("name")//商品的归属名字
	public String name;
	@SerializedName("coupon")//抵扣券（若不能使用抵扣券值为0）
	public int coupon;
	@SerializedName("goods_cost")//商品成本价（总部商品和不能使用抵扣券时值默认为0）
	public String goods_cost;


}

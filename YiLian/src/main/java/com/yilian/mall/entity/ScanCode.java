package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class ScanCode {
	
	
	private String type;//
	private String name;//兑换中心名字
	private String order_id;//订单编号
	private String order_integral;//需要支付的奖券数量
	
	private String activity_name;//兑换中心名字
	private String activity_id;//活动唯一id
	private String activity_type;//活动类型，1转盘、4代表摇一摇、2代表大家猜、3代表刮刮乐
	
	private String merchant_name;//联盟商家名字
	private String merchant_id;//联盟商家唯一id
	
	private String filiale_name;//兑换中心名字
	
	private String given_index;//奖券赠送活动编号
	private String filiale_id;//兑换中心唯一id
	
	/**
	 * 二维码类型
	 */
	public String Type;
	
	/**
	 * 0 表示财付通
	 */
	@SerializedName("payment_type")
	public int payMentType;
	
	/**
	 * 需要支付的商家订单号
	 */
	@SerializedName("payment_order")
	public String payMentOrder;
	
	/**
	 * 需要支付的总金额(单位：分RMB)
	 */
	@SerializedName("payment_money")
	public String payMentMoney;
	
	/**
	 * 商家名字
	 */
	@SerializedName("merchant_name")
	public String merchantName;
	
	/**
	 * 商家id
	 */
	@SerializedName("merchant_id")
	public String merchantId;
	
	/**
	 * 商家手机号
	 */
	@SerializedName("merchant_phone")
	public String merchantPhone;
	
	/**
	 * 商家手机号
	 */
	@SerializedName("merchant_url")
	public String merchantUrl;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_integral() {
		return order_integral;
	}
	public void setOrder_integral(String order_integral) {
		this.order_integral = order_integral;
	}
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}
	public String getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}
	public String getMerchant_name() {
		return merchant_name;
	}
	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}
	public String getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getFiliale_name() {
		return filiale_name;
	}
	public void setFiliale_name(String filiale_name) {
		this.filiale_name = filiale_name;
	}
	public String getGiven_index() {
		return given_index;
	}
	public void setGiven_index(String given_index) {
		this.given_index = given_index;
	}
	public String getFiliale_id() {
		return filiale_id;
	}
	public void setFiliale_id(String filiale_id) {
		this.filiale_id = filiale_id;
	}
	
	

}

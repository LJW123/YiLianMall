package com.yilian.mall.entity;

import java.io.Serializable;

public class PayConfigurationBean implements Serializable {

	String name;
	String seller;
	String partner;// 支付宝PID
	String rsa_private;// 商户密钥 pkcs8格式

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getRsa_private() {
		return rsa_private;
	}

	public void setRsa_private(String rsa_private) {
		this.rsa_private = rsa_private;
	}
	
	private String wxzh;
	private String app_id;
	private String mchid;
	private String partner_key;

	public String getWxzh() {
		return wxzh;
	}

	public void setWxzh(String wxzh) {
		this.wxzh = wxzh;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getPartner_key() {
		return partner_key;
	}

	public void setPartner_key(String partner_key) {
		this.partner_key = partner_key;
	}
	
	
	

}

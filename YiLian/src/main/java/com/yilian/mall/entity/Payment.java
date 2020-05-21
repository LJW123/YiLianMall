package com.yilian.mall.entity;

public class Payment extends BaseEntity{

    private String intrgral; //积发奖励包
    private String deal_id; //交易流水号
    private String deal_time; //交易时间
	public String getIntrgral() {
		return intrgral;
	}
	public void setIntrgral(String intrgral) {
		this.intrgral = intrgral;
	}
	public String getDeal_id() {
		return deal_id;
	}
	public void setDeal_id(String deal_id) {
		this.deal_id = deal_id;
	}
	public String getDeal_time() {
		return deal_time;
	}
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}
	
	

}

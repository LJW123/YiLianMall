package com.yilian.mall.entity;

public class PrizeVoucherDetailBean {
	
	public String code;//服务器处理结果
	public String voucher_prize_name;//奖品名字
	public String voucher_grant_time;//中奖时间
	public String voucher_valid_time;//过期时间
	public String prize_status;//凭证状态(0未使用，1还需要）
	public String voucher_vocher_secret;//凭证密钥，用于生成条码或二维码
	public String voucher_filiale_name;//商家名字
	public String voucher_address;//兑换地址
	public String voucher_tel;//联系电话
	public int voucher_type;//活动类型
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVoucher_prize_name() {
		return voucher_prize_name;
	}
	public void setVoucher_prize_name(String voucher_prize_name) {
		this.voucher_prize_name = voucher_prize_name;
	}
	public String getVoucher_grant_time() {
		return voucher_grant_time;
	}
	public void setVoucher_grant_time(String voucher_grant_time) {
		this.voucher_grant_time = voucher_grant_time;
	}
	public String getVoucher_valid_time() {
		return voucher_valid_time;
	}
	public void setVoucher_valid_time(String voucher_valid_time) {
		this.voucher_valid_time = voucher_valid_time;
	}
	public String getPrize_status() {
		return prize_status;
	}
	public void setPrize_status(String prize_status) {
		this.prize_status = prize_status;
	}
	
	public String getVoucher_vocher_secret() {
		return voucher_vocher_secret;
	}
	public void setVoucher_vocher_secret(String voucher_vocher_secret) {
		this.voucher_vocher_secret = voucher_vocher_secret;
	}
	public String getVoucher_filiale_name() {
		return voucher_filiale_name;
	}
	public void setVoucher_filiale_name(String voucher_filiale_name) {
		this.voucher_filiale_name = voucher_filiale_name;
	}
	public String getVoucher_address() {
		return voucher_address;
	}
	public void setVoucher_address(String voucher_address) {
		this.voucher_address = voucher_address;
	}
	public String getVoucher_tel() {
		return voucher_tel;
	}
	public void setVoucher_tel(String voucher_tel) {
		this.voucher_tel = voucher_tel;
	}

	public int getVoucher_type() {
		return voucher_type;
	}
}

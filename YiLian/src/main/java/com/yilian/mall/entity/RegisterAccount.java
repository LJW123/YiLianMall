package com.yilian.mall.entity;


public class RegisterAccount extends BaseEntity {
	//注册时验证短信码并注册账号实体类
	
	/**
	 * 服务器返回的token值
	 */
	public String token;
	
	/**
	 * 新用户注册赠送奖券 0 标示没有赠送奖券
	 */
	public String given_integral;
	
	/**
     * 账户积发奖励包
     */
    public String integral_balance;

	/**
	 * 新用户注册时获赠的抵扣券的数量
	 */
	public String given_coupon;

    @Override
    public String toString() {
        return "RegisterAccount{" +
                "code="+code+ '\'' +
                "token='" + token + '\'' +
                ", given_integral='" + given_integral + '\'' +
                ", integral_balance='" + integral_balance + '\'' +
                ", given_coupon='" + given_coupon + '\'' +
                '}';
    }
}

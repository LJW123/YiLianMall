package com.yilian.mall.entity;

public class User_info extends BaseEntity{

	private String phone;//用户手机号
	private String name;//用户昵称
	private String sex;//用户性别(0未知，1男，2女)
	private String integral;//用户当前乐分数量
	private String birthday;//用户生日时间戳（单位：秒）
	private String province;//用户所在省份
	private String city;//用户所在城市
	private String district;//用户所在区、县
	private String address;//用户详细地址
	private String intro;//用户个性签名
	private String photo;//用户头像url
	private String card_index;//身份证资料编号
	private String card_name;//用户真实姓名（未通过审核时为空值）
	private boolean pay_password;//用户是否已经设置支付密码
	private boolean login_password;//用户是否已经设置登录密码，可以用来确认用户是否是快捷登录
    private String lebi;//乐币奖励
    private String coupon;//乐券奖励

	public String getLebi() {
		return lebi;
	}
	public void setLebi(String lebi) {
		this.lebi = lebi;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCard_index() {
		return card_index;
	}
	public void setCard_index(String card_index) {
		this.card_index = card_index;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public boolean getPay_password() {
		return pay_password;
	}
	public void setPay_password(boolean pay_password) {
		this.pay_password = pay_password;
	}
	public boolean getLogin_password() {
		return login_password;
	}
	public void setLogin_password(boolean login_password) {
		this.login_password = login_password;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
}

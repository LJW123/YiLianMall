package com.yilian.mall.entity;

public class MainActivityList {
	
	private String activity_index; //活动唯一id
	private String activity_type; //活动类型，1转盘、4代表摇一摇、2代表大家猜、3代表刮刮乐
	private String activity_name; //活动名字标题
	private String activity_sponsor_name; //活动主办方
	private String activity_city; //活动城市
	private String activity_county; //活动区县
	private String activity_status; //活动状态，0表示未开始，1表示已经开始,3表示活动已经结束
	private String activity_start_time; //活动开始时间
	private String activity_server_time; //服务器当前时间
	private String activity_end_time; //活动结束时间
	private String activity_prize_url; //活动奖品url（前缀：cdn.image.huyongle.com）
	private int activity_total_count;//活动需要参与的总次数（非大家猜活动时忽略此字段）
	private int activity_play_count; //活动已参与总次数
	private String activity_expend; //参加一次活动需要的奖券数量
	
	
	public int getActivity_total_count() {
		return activity_total_count;
	}
	public void setActivity_total_count(int activity_total_count) {
		this.activity_total_count = activity_total_count;
	}
	public String getActivity_index() {
		return activity_index;
	}
	public void setActivity_index(String activity_index) {
		this.activity_index = activity_index;
	}
	public String getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getActivity_sponsor_name() {
		return activity_sponsor_name;
	}
	public void setActivity_sponsor_name(String activity_sponsor_name) {
		this.activity_sponsor_name = activity_sponsor_name;
	}
	public String getActivity_city() {
		return activity_city;
	}
	public void setActivity_city(String activity_city) {
		this.activity_city = activity_city;
	}
	public String getActivity_county() {
		return activity_county;
	}
	public void setActivity_county(String activity_county) {
		this.activity_county = activity_county;
	}
	public String getActivity_status() {
		return activity_status;
	}
	public void setActivity_status(String activity_status) {
		this.activity_status = activity_status;
	}
	public String getActivity_start_time() {
		return activity_start_time;
	}
	public void setActivity_start_time(String activity_start_time) {
		this.activity_start_time = activity_start_time;
	}
	public String getActivity_end_time() {
		return activity_end_time;
	}
	public void setActivity_end_time(String activity_end_time) {
		this.activity_end_time = activity_end_time;
	}
	public String getActivity_prize_url() {
		return activity_prize_url;
	}
	public void setActivity_prize_url(String activity_prize_url) {
		this.activity_prize_url = activity_prize_url;
	}
	public int getActivity_play_count() {
		return activity_play_count;
	}
	public void setActivity_play_count(int activity_play_count) {
		this.activity_play_count = activity_play_count;
	}
	public String getActivity_expend() {
		return activity_expend;
	}
	public void setActivity_expend(String activity_expend) {
		this.activity_expend = activity_expend;
	}
	public String getActivity_server_time() {
		return activity_server_time;
	}
	public void setActivity_server_time(String activity_server_time) {
		this.activity_server_time = activity_server_time;
	}
	
}

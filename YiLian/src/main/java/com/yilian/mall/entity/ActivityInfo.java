package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityInfo {
	
	/**
	 * 活动唯一id
	 */
	@SerializedName("activity_index")
	public String activityIndex;

	/**
	 * 活动名字标题
	 */
	@SerializedName("activity_name")
	public String activityName;
	
	/**
	 * 活动类型，1转盘、4代表摇一摇、2代表大家猜、3代表刮刮乐
	 */
	@SerializedName("activity_type")
	public int activityType;

	/**
	 * 活动开始时间
	 */
	@SerializedName("activity_start_time")
	public long startTime;//活动开始时间
	
	
	/**
	 * 服务器当前时间
	 */
	@SerializedName("activity_server_time")
	public long serverTime;
	
	/**
	 * 活动状态，0表示未开始，1表示已经开始
	 */
	@SerializedName("activity_status")
    public int status;//
	
	/**
	 * 参加一次活动需要的奖券数量
	 */
	@SerializedName("activity_expend")
    public int expend;//参加一次活动需要的奖券数量
	
	/**
	 * 活动已参与总次数
	 */
	@SerializedName("activity_play_count")
    public int playCount;//活动已参与总次数
	
	/**
	 * 活动已参与总次数
	 */
	@SerializedName("activity_total_count")
    public int totalCount;//活动已参与总次数
	
	/**
	 * 活动奖品url（前缀：cdn.image.huyongle.com）
	 */
	@SerializedName("activity_prize_url")
    public String prizeUrl;//活动奖品url（前缀：cdn.image.huyongle.com）
	
	/**
	 * 活动主办方
	 */
	@SerializedName("activity_sponsor_name")
	public String sponsorName;
	
	/**
	 * 联系人手机号
	 */
	@SerializedName("activity_phone")
	public String phone;
	
	/**
	 * /联系电话（座机）
	 */
	@SerializedName("activity_tel")
	public String tel;
	
	/**
	 * 兑奖地址
	 */
	@SerializedName("activity_address")
	public String address;
	
	/**
	 * 中奖凭证有效天数（单位秒，需要客户端转换）
	 */
	@SerializedName("activity_time_limit")
	public int timeLimit;
	
	/**
	 * 活动联系人
	 */
	@SerializedName("activity_contacts")
	public String contacts;
	
	/**
	 * 活动发布城市id
	 */
	@SerializedName("activity_city")
	public String cityId;
	
	/**
	 * 活动发布区县
	 */
	@SerializedName("activity_county")
	public String countyId; //活动区县
	
	/**
	 * 奖品列表
	 */
	@SerializedName("activity_prize")
	public List<ActivityPrize> prizes;
}

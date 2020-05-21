package com.yilian.mall.entity;

/**
 * 参加刮刮乐活动抽奖结果
 */
public class ScrapeEntity extends BaseEntity{
	public String activity_index;//活动id
	public int integral;//用户剩余乐分数量
	public int prize_levle;//获奖位置（0-5表示一到六等奖）
	public int prize_type;//奖品类型，0表示虚拟，1表示实物
	public String prize_name;//获奖商品名字
	public int prize_voucher;//实物奖凭凭证编号
	public int scrape_count;//此用户已参与次数
}

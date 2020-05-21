package com.yilian.mall.entity;

import java.util.List;

/**
 * 乐券获得记录实体
 */
public class CouponGainListBean extends BaseEntity{
	public List<CouponGainList> list;
	public class CouponGainList{
		public String merchant_name;//商家名字
		public String merchant_goods;//消费品名字
		public String merchant_buy_money;//消费金额（人民币分）
		public String merchant_give_coupon;//获得乐券数量（除以100，保留小数点后两位）
		public String merchant_give_time;//奖券获得时间
	}
}

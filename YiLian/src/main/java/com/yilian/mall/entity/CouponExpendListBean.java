package com.yilian.mall.entity;

import java.util.List;

/**
 * 乐享币兑换记录实体
 */
public class CouponExpendListBean extends BaseEntity{
	public List<CouponExpendList> list;
	public class CouponExpendList{
		public String filiale_name;//兑换中心名字
		public String sell_total_price;//额外支付金额（人民币分）
		public String sell_total_coupon;//付乐券数量
		public String sell_time;//乐券兑换时间

		public CouponExpendList(String filiale_name, String sell_total_price, String sell_total_coupon, String sell_time) {
			this.filiale_name = filiale_name;
			this.sell_total_price = sell_total_price;
			this.sell_total_coupon = sell_total_coupon;
			this.sell_time = sell_time;
		}
	}
}

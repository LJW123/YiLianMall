package com.yilian.mall.entity;

import java.util.List;


public class RecordExchangeJifenListBean extends BaseEntity{

	public List<ExchangeJifenListBean> list;

	public class ExchangeJifenListBean {
		public String filiale_name;//兑换中心名字
		public String sell_cash; //额外支付金额（人民币分）
		public String sell_total_integral; //支付奖券数量
		public String sell_time; //奖券兑换时间
		
	}
}

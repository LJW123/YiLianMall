package com.yilian.mall.entity;

import java.util.List;

public class RecordLegouListBean extends BaseEntity{

	public List<LegouListBean> list;
	public class LegouListBean{
		public String filiale_id;//兑换中心ID
		public String filiale_shop;//兑换中心分店ID
		public String shop_name;//兑换中心分店名字
		public String shop_image;//分店环境照片
		public String consumer_gain_lebi;//在本次交易中获得的乐币数量，同时代表花费的现金数量
		public String payment_succeed_time;//交易时间
	}
}

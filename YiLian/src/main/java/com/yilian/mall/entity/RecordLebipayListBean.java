package com.yilian.mall.entity;

import java.util.List;

public class RecordLebipayListBean extends BaseEntity{
	public List<LebipayListBean> list;
	public class LebipayListBean{
		public String merchant_id;//商家ID
		public String merchant_name;//商家名字
		public String merchant_image;//商家照片
		public String consumer_expend_lebi;//消费乐币数量
		public String deal_time;//交易时间
	}
}

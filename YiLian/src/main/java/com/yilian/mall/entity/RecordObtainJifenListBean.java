package com.yilian.mall.entity;

import java.util.List;

public class RecordObtainJifenListBean extends BaseEntity{
	public List<JifenRecordListBean> list;

	public class JifenRecordListBean {
		
		public String merchant_name;//商家名字
		public String merchant_goods; //消费品名字
		public String merchant_income; //消费金额（人民币分）
		public String merchant_give_integral; //获得奖券数量
		public String merchant_give_time; //奖券获得时间
		
	}

}

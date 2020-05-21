package com.yilian.mall.entity;

import java.util.List;

public class RecordPaticipateActivityListBean extends BaseEntity {
	public List<PaticipateActivityListBean> list;
	
	public class PaticipateActivityListBean {
		public String activity_name;//活动名字
		public String activity_pay; //支付奖券数量
		public String paticipate_time; //活动参与时间
	
	}
}

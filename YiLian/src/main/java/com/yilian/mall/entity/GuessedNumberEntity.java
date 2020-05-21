package com.yilian.mall.entity;

import java.util.List;
public class GuessedNumberEntity extends BaseEntity{

	public List<GuessedNumberEntityList> list;
	public class GuessedNumberEntityList{
		public String guess_figure;//用户猜的数字
		public String guess_count;//用户猜的数字已经被猜测的次数
		public String guess_time;//猜的时间（以秒为单位的时间戳）
	}
}

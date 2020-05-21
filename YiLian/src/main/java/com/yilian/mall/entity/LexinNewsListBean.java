package com.yilian.mall.entity;

import java.util.List;

public class LexinNewsListBean extends BaseEntity{
	public List<LexinNewsList> list;
	public class LexinNewsList{
		public String news_index;//新闻数据编号
		public String news_title;//新闻标题
		public int news_owner_type;//新闻发布方类型：0代表系统消息，1代表兑换中心，2代表乐分总部
		public String news_time;//新闻发布时间
		public int news_type;//新闻内容类型：0显示乐分新闻页、1显示URL外链、2跳转到兑换中心详情页面、3跳转到活动页面、4显示联盟商家页面
		public String news_data;//取决于news_type，分别是：新闻id，外链url，兑换中心分店id，活动类型:活动id，联盟商家id
		public String news_image;//新闻标题图片
	}
}


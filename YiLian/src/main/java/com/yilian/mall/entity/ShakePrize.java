package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShakePrize extends BaseEntity {
	
	public ArrayList<PrizeList> list;

	public static class PrizeList{
		/**
		 * 归属活动唯一id
		 */
		@SerializedName("prize_for_activity")
		public String prizeForActivity;
		
		/**
		 * 奖品名字
		 */
		@SerializedName("prize_name")
		public String prizeName; 
		
		/**
		 * 奖品图片地址
		 */
		@SerializedName("prize_url")
		public String prizeUrl;
		
		/**
		 * 赞助方联系电话
		 */
		@SerializedName("prize_tel")
		public String prizeTel;
		
		/**
		 * 兑奖地址
		 */
		@SerializedName("prize_address")
		public String prizeAddress; 
		
		/**
		 * 奖品赞助商名字
		 */
		@SerializedName("prize_sponsor_name")
		public String prizeSponsorName;
		
		/**
		 * 奖品数量
		 */
		@SerializedName("prize_amount")
		public String prizeAmount;
		
		/**
		 * 商家id
		 */
		@SerializedName("prize_sponsor")
		public String prizeSponsor;
		
		/**
		 * 商家类型
		 */
		@SerializedName("prize_sponsor_type")
		public String prizeSponsorType;

	}

}

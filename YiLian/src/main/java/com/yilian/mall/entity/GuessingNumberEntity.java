package com.yilian.mall.entity;

public class GuessingNumberEntity extends BaseEntity{

		public String guess_figure;//用户猜的数字
		public String guess_count;//用户猜的数字已经被猜测的次数
		public String prize_voucher;//实物类奖品的中奖凭证编号（未中奖时为0）
    public int integral;//用户当前积发奖励包
}

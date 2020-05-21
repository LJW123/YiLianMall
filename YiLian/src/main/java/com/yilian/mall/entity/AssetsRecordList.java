package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yukun on 2016/3/27.
 */
public class AssetsRecordList extends BaseEntity {

    public ArrayList<AssetsRecord> list;

    public class AssetsRecord {

        public String id;//充值记录编号

        @SerializedName("pay_count") // 充值数量  通过充值数量转换出充值金额
        public String payCount;
        @SerializedName("pay_time") // 充值时间
        public String payTime;
        @SerializedName("income") // 充值金额
        public String income;
        @SerializedName("type_msg") // 记录文字描述
        public String typeMsg;

        /**
         * 消费类型：0表示APP乐币订单，1表示会员卡乐币订单。
         * 获得类型：0表示乐购区支付获得，1表示收银系统现金充值获得，2表示app线上充值获得乐享币 。
         * 领奖励记录 类型 0待审核  2领奖励成功
         */
        public int type;
    }
}

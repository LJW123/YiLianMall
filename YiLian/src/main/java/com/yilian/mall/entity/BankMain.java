package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lefenandroid on 16/6/29.
 */
public class BankMain extends BaseEntity {

    /**
     * last_income : 昨日领奖励
     * percentage : 领奖励率
     * income_lebi : 累计领奖励
     * income_log : [{"msg":"","income_time":"","consumer_gain_lebi":"","consumer_after_lebi":""},{}]
     */

    @SerializedName("last_income")
    public String lastIncome;
    @SerializedName("percentage")
    public String percentage;
    @SerializedName("income_lebi")
    public String incomeLebi;

    @SerializedName("avible_lebi")//可转出奖励
    public String avible_lebi;
    /**
     * msg : 领奖励类型
     * income_time : 时间
     * consumer_gain_lebi : 领奖励乐享币数量
     * consumer_after_lebi : 变动后奖励
     */

    @SerializedName("income_log")
    public List<IncomeLogBean> incomeLog;

    public static class IncomeLogBean {
        @SerializedName("msg")
        public String msg;
        @SerializedName("type")
        public int type;
        @SerializedName("income_time")
        public String incomeTime;
        @SerializedName("consumer_gain_lebi")
        public String consumerGainLebi;
        @SerializedName("consumer_after_lebi")
        public String consumerAfterLebi;
        
    }
}

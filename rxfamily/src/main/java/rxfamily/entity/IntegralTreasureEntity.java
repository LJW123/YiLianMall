package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 集分宝首页
 * Created by Administrator on 2018/1/13 0013.
 */

public class IntegralTreasureEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * {
         * integral: "100000",//用户奖券余额  单位分
         * jfb_integral: "100000",//集分宝奖券余额  单位分
         * all_exchange: "2000",//累计兑换  单位分
         * yesterday_exchange: "300",//昨日兑换  单位分
         * exchange_index: '5274',//兑换指数  参照现在处理方式（万分之）
         * jfb_auto_transfer: '0',//是否开启自动转入  0未开启  1开启自动转入
         * jfb_auto_time: '0',//后台设置 集分宝自动转入时间 （例如每天22:00奖券自动转入集分宝，这个参数目前产品那边还没有准确定住）
         * jfb_hold_integral: '5274',//自动转入 保留奖券数  单位：分
         * limit_explain_show: '0',//限额说明  0不展示 1展示
         * limit_explain_url: '',//限额说明H5链接地址
         * jfb_transfer_limit: '20000'//集分宝转入限额   单位：分
         * remain_jfb_integral:'5000'   //剩余可转入奖券  单位：分
         * plan_give_red：'预计发奖励时间（09-26）'
         * is_transfer_limit: 1 //  1不限制转入 2限制转入
         * }
         */
        @SerializedName("jfb_integral")
        private String jfbIntegral;
        @SerializedName("all_exchange")
        private String allExchange;
        @SerializedName("yesterday_exchange")
        private String yesterdayExchange;
        @SerializedName("exchange_index")
        private String exchangeIndex;
        @SerializedName("jfb_auto_transfer")
        private String jfbAutoTransfer;
        @SerializedName("jfb_auto_time")
        private String jfbAutoTime;
        @SerializedName("jfb_hold_integral")
        private String jfbHoldIntegral;
        @SerializedName("integral")
        private String integral;
        @SerializedName("limit_explain_show")
        private String limitExplainShow;
        @SerializedName("limit_explain_url")
        private String limitExplainUrl;
        @SerializedName("jfb_transfer_limit")
        private String jfbTransferLimit;
        @SerializedName("remain_jfb_integral")
        private String remainJfbIntegral;
        @SerializedName("plan_give_red")
        private String planGiveRed;
        @SerializedName("is_transfer_limit")
        private String isTransferLimit;

        public String getJfbIntegral() {
            return jfbIntegral;
        }

        public void setJfbIntegral(String jfbIntegral) {
            this.jfbIntegral = jfbIntegral;
        }

        public String getAllExchange() {
            return allExchange;
        }

        public void setAllExchange(String allExchange) {
            this.allExchange = allExchange;
        }

        public String getYesterdayExchange() {
            return yesterdayExchange;
        }

        public void setYesterdayExchange(String yesterdayExchange) {
            this.yesterdayExchange = yesterdayExchange;
        }

        public String getExchangeIndex() {
            return exchangeIndex;
        }

        public void setExchangeIndex(String exchangeIndex) {
            this.exchangeIndex = exchangeIndex;
        }

        public String getJfbAutoTransfer() {
            return jfbAutoTransfer;
        }

        public void setJfbAutoTransfer(String jfbAutoTransfer) {
            this.jfbAutoTransfer = jfbAutoTransfer;
        }

        public String getJfbAutoTime() {
            return jfbAutoTime;
        }

        public void setJfbAutoTime(String jfbAutoTime) {
            this.jfbAutoTime = jfbAutoTime;
        }

        public String getJfbHoldIntegral() {
            return jfbHoldIntegral;
        }

        public void setJfbHoldIntegral(String jfbHoldIntegral) {
            this.jfbHoldIntegral = jfbHoldIntegral;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getLimitExplainShow() {
            return limitExplainShow;
        }

        public void setLimitExplainShow(String limitExplainShow) {
            this.limitExplainShow = limitExplainShow;
        }

        public String getLimitExplainUrl() {
            return limitExplainUrl;
        }

        public void setLimitExplainUrl(String limitExplainUrl) {
            this.limitExplainUrl = limitExplainUrl;
        }

        public String getJfbTransferLimit() {
            return jfbTransferLimit;
        }

        public void setJfbTransferLimit(String jfbTransferLimit) {
            this.jfbTransferLimit = jfbTransferLimit;
        }

        public String getRemainJfbIntegral() {
            return remainJfbIntegral;
        }

        public void setRemainJfbIntegral(String remainJfbIntegral) {
            this.remainJfbIntegral = remainJfbIntegral;
        }

        public String getPlanGiveRed() {
            return planGiveRed;
        }

        public void setPlanGiveRed(String planGiveRed) {
            this.planGiveRed = planGiveRed;
        }

        public String getIsTransferLimit() {
            return isTransferLimit;
        }

        public void setIsTransferLimit(String isTransferLimit) {
            this.isTransferLimit = isTransferLimit;
        }
    }
}

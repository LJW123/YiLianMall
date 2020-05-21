package rxfamily.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 钱包 交易记录
 * Created by Administrator on 2018/1/13 0013.
 */

public class TransactionRecordEntity extends BaseEntity {

    @SerializedName("data")//记录
    public TransactionRecordEntity.Data data;

    public class Data {
        @SerializedName("statistics")
        private List<StatisticsBean> statistics;
        @SerializedName("list")
        private List<ListBean> list;

        public List<StatisticsBean> getStatistics() {
            return statistics;
        }

        public void setStatistics(List<StatisticsBean> statistics) {
            this.statistics = statistics;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
    }


    public static class StatisticsBean {
        /**
         * "integral_expend": "171", // 奖券支出
         * "integral_income": "0", // 奖券收入
         * "cash_expend": "0", // 余额支出
         * "cash_income": "0", // 余额收入
         * "date": "2018-01",
         * "year": "2018",
         * "month": "01"
         */

        @SerializedName("integral_expend")
        private String integralExpend;
        @SerializedName("integral_income")
        private String integralIncome;
        @SerializedName("cash_expend")
        private String cashExpend;
        @SerializedName("cash_income")
        private String cashIncome;
        @SerializedName("date")
        private String date;
        @SerializedName("year")
        private String year;
        @SerializedName("month")
        private String month;

        public String getIntegralExpend() {
            return integralExpend;
        }

        public void setIntegralExpend(String integralExpend) {
            this.integralExpend = integralExpend;
        }

        public String getIntegralIncome() {
            return integralIncome;
        }

        public void setIntegralIncome(String integralIncome) {
            this.integralIncome = integralIncome;
        }

        public String getCashExpend() {
            return cashExpend;
        }

        public void setCashExpend(String cashExpend) {
            this.cashExpend = cashExpend;
        }

        public String getCashIncome() {
            return cashIncome;
        }

        public void setCashIncome(String cashIncome) {
            this.cashIncome = cashIncome;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }

    public static class ListBean {
        /**
         * "flag": "1", // 1奖券变更  0余额变更
         * "id": "415620", // 变更记录id
         * "amount": "23", // 更变数值
         * "type": "102", // 变更类型
         * "order": "2018011003000261004",
         * "time": "1515524402", // 变更时间
         * "consumer": "8888888888810000",
         * "uid": "0",
         * "img": "jifen.png", // 图片地址
         * "type_msg": "发奖励扣除", //
         * "year": "2018",
         * "month": "01",
         * "date": "2018-01"
         */

        @SerializedName("flag")
        private String flag;
        @SerializedName("id")
        private String id;
        @SerializedName("amount")
        private String amount;
        @SerializedName("type")
        private String type;
        @SerializedName("order")
        private String order;
        @SerializedName("time")
        private String time;
        @SerializedName("consumer")
        private String consumer;
        @SerializedName("uid")
        private String uid;
        @SerializedName("img")
        private String img;
        @SerializedName("type_msg")
        private String typeMsg;
        @SerializedName("year")
        private String year;
        @SerializedName("month")
        private String month;
        @SerializedName("date")
        private String date;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getConsumer() {
            return consumer;
        }

        public void setConsumer(String consumer) {
            this.consumer = consumer;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTypeMsg() {
            return typeMsg;
        }

        public void setTypeMsg(String typeMsg) {
            this.typeMsg = typeMsg;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}

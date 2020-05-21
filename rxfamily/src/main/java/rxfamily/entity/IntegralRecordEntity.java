package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 集分宝模块 明细记录
 * Created by Administrator on 2018/1/13 0013.
 */

public class IntegralRecordEntity extends BaseEntity {

    @SerializedName("statistics")//统计性数据  所有列表最后一条数据到当前时间的所有的按月份的统计数据
    public List<Statistics> statistics;
    @SerializedName("list")//记录
    public List<Record> list;

    public class Statistics {
        /**
         * {
         * "expend": "219432141", // 奖券转出
         * "income": "596", // 奖券转入
         * "red_expend":'5428' ,// 拆奖励扣除
         * "date": "2018-01",
         * "year":"2018",
         * "month":"1",
         * }
         */

        @SerializedName("expend")
        private String expend;
        @SerializedName("income")
        private String income;
        @SerializedName("red_expend")
        private String redExpend;
        @SerializedName("date")
        private String date;
        @SerializedName("year")
        private String year;
        @SerializedName("month")
        private String month;

        public String getExpend() {
            return expend;
        }

        public void setExpend(String expend) {
            this.expend = expend;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getRedExpend() {
            return redExpend;
        }

        public void setRedExpend(String redExpend) {
            this.redExpend = redExpend;
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

    public static class Record {

        /**
         * id : 415821
         * type : 102
         * amount : 28259414
         * order : 2018011103000641282
         * time : 1515610806
         * img : app/images/tubiao/pingtai.png
         * type_msg : 拆奖励扣除
         * year : 2018
         * month : 01
         * date : 2018-01
         */

        @SerializedName("id")
        private String id;
        @SerializedName("type")//小于 100 是"+"  大于100 是"-"
        private String type;
        @SerializedName("amount")
        private String amount;
        @SerializedName("order")
        private String order;
        @SerializedName("time")
        private String time;
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

        /**
         * "expend" // 奖券转出
         * "income" // 奖券转入
         * "red_expend"// 拆奖励扣除
         */
        private String expend;
        private String income;
        private String redExpend;

        /**
         * 是否展开
         */
        private Boolean unfold = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public String getExpend() {
            return expend;
        }

        public void setExpend(String expend) {
            this.expend = expend;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getRedExpend() {
            return redExpend;
        }

        public void setRedExpend(String redExpend) {
            this.redExpend = redExpend;
        }

        public Boolean getUnfold() {
            return unfold;
        }

        public void setUnfold(Boolean unfold) {
            this.unfold = unfold;
        }
    }

}

package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 集分宝模块 累计兑换明细记录
 * Created by Administrator on 2018/1/13 0013.
 */

public class IntegralExchangeRecordEntity extends BaseEntity {

    @SerializedName("data")//记录
    public List<Data> dataList;


    public static class Data {
        /**
         * {
         * "id": "2428023", // 记录id
         * "type": "7", // 变更类型  可用于确定该次变更的正负   大于100为负值，小于100为正值
         * "amount": "78066556", // 变更金额  客户端需除以100
         * "order": "2018011803000346059", // 本次变更关联id  客户端暂时用不到
         * "time": "1516251195", // 变更时间 ，用于各种时间量的显示和计算
         * "img": "app/images/tubiao/jifen.png", // 变更关联图片 ，前缀参照统一前缀
         * "type_msg": "拆奖励获得", // 变更类型
         * "year": "2018", // 年
         * "month": "01", // 月
         * "date": "2018-01" // 日期， 用于客户端月份统计时候做比较
         * }
         */

        @SerializedName("id")
        private String id;
        @SerializedName("type")
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
    }

}

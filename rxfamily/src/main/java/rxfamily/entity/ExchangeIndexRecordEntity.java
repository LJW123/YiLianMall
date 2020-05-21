package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 集分宝模块 指数记录
 * Created by Administrator on 2018/1/13 0013.
 */

public class ExchangeIndexRecordEntity extends BaseEntity {


    @SerializedName("data")//记录
    public Data data;

    public class Data {
        /**
         * index:'',//用户当前消费指数 （同集分宝首页奖券指数）
         * last_seven:[]//近七天指数
         */

        @SerializedName("index")
        private String index;
        @SerializedName("last_seven")
        private List<LastSevenBean> lastSeven;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public List<LastSevenBean> getLastSeven() {
            return lastSeven;
        }

        public void setLastSeven(List<LastSevenBean> lastSeven) {
            this.lastSeven = lastSeven;
        }
    }

    public static class LastSevenBean {
        /**
         * {
         * time:'',//日期
         * index:'',//指数
         * rise_fall:''//日涨跌幅
         * plusorsub:1 //1涨 2跌  0 平
         * }
         */

        @SerializedName("time")
        private String time;
        @SerializedName("index")
        private String index;
        @SerializedName("rise_fall")
        private String riseFall;
        @SerializedName("plusorsub")
        private String plusorsub;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getRiseFall() {
            return riseFall;
        }

        public void setRiseFall(String riseFall) {
            this.riseFall = riseFall;
        }

        public String getPlusorsub() {
            return plusorsub;
        }

        public void setPlusorsub(String plusorsub) {
            this.plusorsub = plusorsub;
        }
    }

}

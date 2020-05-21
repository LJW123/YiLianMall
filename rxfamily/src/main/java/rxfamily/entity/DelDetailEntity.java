package rxfamily.entity;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class DelDetailEntity {

    /**
     * code : 1
     * msg : success
     * data : {"statistics":[{"integral_expend":"171","integral_income":"0","cash_expend":"0","cash_income":"0","date":"2018-01","year":"2018","month":"01"}],"list":[{"flag":"1","id":"415620","amount":"23","type":"102","order":"2018011003000261004","time":"1515524402","consumer":"8888888888810000","uid":"0","img":"jifen.png","type_msg":"发奖励扣除","year":"2018","month":"01","date":"2018-01"},{"flag":"1","id":"415620","amount":"19","type":"102","order":"2018010903000223444","time":"1515438003","consumer":"8888888888810000","uid":"0","img":"jifen.png","type_msg":"发奖励扣除","year":"2018","month":"01","date":"2018-01"},{"flag":"1","id":"415620","amount":"20","type":"102","order":"2018010803000297726","time":"1515351602","consumer":"8888888888810000","uid":"0","img":"jifen.png","type_msg":"发奖励扣除","year":"2018","month":"01","date":"2018-01"}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public static DelDetailEntity objectFromData(String str) {
        return new Gson().fromJson(str, DelDetailEntity.class);
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<StatisticsBean> statistics;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public List<StatisticsBean> getStatistics() {
            return statistics;
        }

        public void setStatistics(List<StatisticsBean> statistics) {
            this.statistics = statistics;
        }

        public static class StatisticsBean {
            /**
             * integral_expend : 171
             * integral_income : 0
             * cash_expend : 0
             * cash_income : 0
             * date : 2018-01
             * year : 2018
             * month : 01
             */

            private String integral_expend;
            private String integral_income;
            private String cash_expend;
            private String cash_income;
            private String date;
            private String year;
            private String month;

            public static StatisticsBean objectFromData(String str) {

                return new Gson().fromJson(str, StatisticsBean.class);
            }

            public static class ListBean {
                /**
                 * flag : 1
                 * id : 415620
                 * amount : 23
                 * type : 102
                 * order : 2018011003000261004
                 * time : 1515524402
                 * consumer : 8888888888810000
                 * uid : 0
                 * img : jifen.png
                 * type_msg : 发奖励扣除
                 * year : 2018
                 * month : 01
                 * date : 2018-01
                 */

                private String flag;
                private String id;
                private String amount;
                private String type;
                private String order;
                private String time;
                private String consumer;
                private String uid;
                private String img;
                private String type_msg;
                private String year;
                private String month;
                private String date;

                public static ListBean objectFromData(String str) {

                    return new Gson().fromJson(str, ListBean.class);
                }
            }
        }
    }
}

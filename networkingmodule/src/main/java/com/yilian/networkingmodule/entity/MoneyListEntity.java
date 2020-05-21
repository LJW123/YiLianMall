package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/12/13.
 */

public class MoneyListEntity extends HttpResultBean {

    /**
     * expend : 319900
     * income : 15829527
     * list : [{"balance_status":"","balance_text":"","date":"2017-12","id":"2420496","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017121203000446889","pay_count":"20246","pay_time":"1513047824","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420495","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017121103000599218","pay_count":"36174","pay_time":"1513047824","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420494","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120910290798596","pay_count":"20592","pay_time":"1513047824","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420493","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120703000616031","pay_count":"20683","pay_time":"1513047824","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420492","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120117193612291","pay_count":"20931","pay_time":"1513047824","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420135","img":"app/images/tubiao/dingdan.png","merchant_uid":"6293120596103802","month":"12","order_id":"2017121114453371340","pay_count":"900","pay_time":"1512974778","refuse_text":"","type":"102","type_msg":"奖励支付","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420128","img":"app/images/tubiao/dingdan.png","merchant_uid":"6293120596103802","month":"12","order_id":"2017121114003965521","pay_count":"900","pay_time":"1512972042","refuse_text":"","type":"102","type_msg":"奖励支付","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420126","img":"app/images/tubiao/dingdan.png","merchant_uid":"6713908494569117","month":"12","order_id":"2017121113590323455","pay_count":"67500","pay_time":"1512971955","refuse_text":"","type":"102","type_msg":"奖励支付","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2420125","img":"app/images/tubiao/dingdan.png","merchant_uid":"6713908494569117","month":"12","order_id":"2017121113533543054","pay_count":"900","pay_time":"1512971627","refuse_text":"","type":"102","type_msg":"奖励支付","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2419238","img":false,"merchant_uid":"6990127121924712","month":"12","order_id":"2017120915034423141","pay_count":"1000","pay_time":"1512803034","refuse_text":"","type":"106","type_msg":"扫码消费","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2419207","img":"app/images/tubiao/dingdan.png","merchant_uid":"6293120596103802","month":"12","order_id":"2017120911001176339","pay_count":"24900","pay_time":"1512788425","refuse_text":"","type":"102","type_msg":"奖励支付","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2418650","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120803000667166","pay_count":"20651","pay_time":"1512695579","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2417713","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120603000478972","pay_count":"20733","pay_time":"1512526976","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2417268","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120503000659420","pay_count":"20782","pay_time":"1512435455","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-12","id":"2416894","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"12","order_id":"2017120403000533601","pay_count":"37013","pay_time":"1512376358","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-11","id":"2413824","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"11","order_id":"2017112710000690718","pay_count":"66693","pay_time":"1511865428","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-11","id":"2413823","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"11","order_id":"2017112311403221763","pay_count":"38607","pay_time":"1511865392","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-11","id":"2413822","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"11","order_id":"2017112710000690720","pay_count":"66693","pay_time":"1511865321","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-11","id":"2413821","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"11","order_id":"2017112710000690719","pay_count":"66693","pay_time":"1511865262","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"},{"balance_status":"","balance_text":"","date":"2017-11","id":"2413818","img":"app/images/tubiao/hongbao.png","merchant_uid":"0","month":"11","order_id":"2017112808422178966","pay_count":"21357","pay_time":"1511864955","refuse_text":"","type":"7","type_msg":"拆奖励获得","year":"2017"}]
     * sql : SELECT SUM(IF(type > 100, amount, 0)) expend, SUM(IF(type < 100, amount, 0)) income, FROM_UNIXTIME(time, '%Y-%m') date, FROM_UNIXTIME(time, '%Y') year, FROM_UNIXTIME(time, '%m') month FROM user_cash_change where time >= 1509465600 and consumer = 6896571039863102 GROUP BY FROM_UNIXTIME(time, '%Y-%m') ORDER BY date DESC LIMIT 10
     * statistics : [{"date":"2017-12","expend":"96100","income":"217805","month":"12","year":"2017"},{"date":"2017-11","expend":"45900","income":"14140625","month":"11","year":"2017"}]
     */

    @SerializedName("expend")
    public String expend;
    @SerializedName("income")
    public String income;
    @SerializedName("sql")
    public String sql;
    @SerializedName("list")
    public List<ListBean> list;
    @SerializedName("statistics")
    public List<StatisticsBean> statistics;

    public static class ListBean extends V3MoneyListBaseData {
        /**
         * balance_status :
         * balance_text :
         * date : 2017-12
         * id : 2420496
         * img : app/images/tubiao/hongbao.png
         * merchant_uid : 0
         * month : 12
         * order_id : 2017121203000446889
         * pay_count : 20246
         * pay_time : 1513047824
         * refuse_text :
         * type : 7
         * type_msg : 拆奖励获得
         * year : 2017
         */

        @SerializedName("balance_status")
        public String balanceStatus;
        @SerializedName("balance_text")
        public String balanceText;
        @SerializedName("date")
        public String date;
        @SerializedName("id")
        public String id;
        @SerializedName("img")
        public String img;
        @SerializedName("merchant_uid")
        public String merchantUid;
        @SerializedName("month")
        public int month;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("pay_count")
        public String payCount;
        @SerializedName("pay_time")
        public long payTime;
        @SerializedName("refuse_text")
        public String refuseText;
        /**
         * 大于100是支出 小于100是收入
         */
        @SerializedName("type")
        public int type;
        @SerializedName("type_msg")
        public String typeMsg;
        @SerializedName("year")
        public int year;

        public String isMoney;//0 奖励 1 奖券

        @Override
        public int getItemType() {
            return V3MoneyListBaseData.LIST_CONTENT;
        }

        public String getIsMoney() {
            return isMoney;
        }

        public void setIsMoney(String isMoney) {
            this.isMoney = isMoney;
        }
    }

    public static class StatisticsBean extends V3MoneyListBaseData {
        /**
         * date : 2017-12
         * expend : 96100
         * income : 217805
         */

        @SerializedName("date")
        public String date;
        @SerializedName("expend")
        public String expend;
        @SerializedName("income")
        public String income;

        public String isMoney;//0 奖励 1 奖券

        @Override
        public int getItemType() {
            return V3MoneyListBaseData.LIST_TITLE;
        }

        public String getIsMoney() {
            return isMoney;
        }

        public void setIsMoney(String isMoney) {
            this.isMoney = isMoney;
        }
    }
}

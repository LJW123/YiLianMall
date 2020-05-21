package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2017/12/14.
 *         奖励、奖券来往记录
 */

public class V3MoneyDealingsEntity extends HttpResultBean {

    /**
     * id : 2420838
     * sql : SELECT * FROM user_cash_change ucc WHERE consumer = 6990127121924712  AND merchant_uid = 6300277240877414 and type in (5,8,102) order by time desc limit 0, 20
     * statistics : [{"expend":"9700","income":"0","date":"2017-12","year":"2017","month":"12"},{"expend":"9800","income":"0","date":"2017-10","year":"2017","month":"10"}]
     * data : [{"index":"2420838","consumer":"6990127121924712","before":"89686252","later":"89676552","amount":"9700","type":"102","merchant_uid":"6300277240877414","order":"2017121316465721156","agency_uid":"89","platform":"0","time":"1513154821","link_user":"0","supplier_id":"146","content":"路虎","year":"2017","month":"12","date":"2017-12"},{"index":"40621","consumer":"6990127121924712","before":"78498016","later":"78493116","amount":"4900","type":"102","merchant_uid":"6300277240877414","order":"2017101816263460722","agency_uid":"89","platform":"0","time":"1508316265","link_user":"0","supplier_id":"146","content":"iPhone X","year":"2017","month":"10","date":"2017-10"},{"index":"40619","consumer":"6990127121924712","before":"78502916","later":"78498016","amount":"4900","type":"102","merchant_uid":"6300277240877414","order":"2017101816263460722","agency_uid":"89","platform":"0","time":"1508316204","link_user":"0","supplier_id":"146","content":"iPhone X","year":"2017","month":"10","date":"2017-10"}]
     */

    @SerializedName("id")
    public String id;
    @SerializedName("sql")
    public String sql;
    @SerializedName("statistics")
    public List<StatisticsBean> statistics;
    @SerializedName("data")
    public List<DataBean> data;

    public static class StatisticsBean extends V3MoneyListBaseData {
        /**
         * expend : 9700
         * income : 0
         * date : 2017-12
         * year : 2017
         * month : 12
         */

        @SerializedName("expend")
        public String expend;
        @SerializedName("income")
        public String income;
        @SerializedName("date")
        public String date;
//        @SerializedName("year")
//        public String year;
//        @SerializedName("month")
//        public int month;

        @Override
        public int getItemType() {
            return V3MoneyListBaseData.LIST_TITLE;
        }
    }

    public static class DataBean extends V3MoneyListBaseData {
        /**
         * index : 2420838
         * consumer : 6990127121924712
         * before : 89686252
         * later : 89676552
         * amount : 9700
         * type : 102
         * merchant_uid : 6300277240877414
         * order : 2017121316465721156
         * agency_uid : 89
         * platform : 0
         * time : 1513154821
         * link_user : 0
         * supplier_id : 146
         * content : 路虎
         * year : 2017
         * month : 12
         * date : 2017-12
         */

        @SerializedName("index")
        public String index;
        @SerializedName("consumer")
        public String consumer;
        @SerializedName("before")
        public String before;
        @SerializedName("later")
        public String later;
        @SerializedName("amount")
        public String amount;
        @SerializedName("type")
        public int type;
        @SerializedName("merchant_uid")
        public String merchantUid;
        @SerializedName("order")
        public String order;
        @SerializedName("agency_uid")
        public String agencyUid;
        @SerializedName("platform")
        public String platform;
        @SerializedName("time")
        public long time;
        @SerializedName("link_user")
        public String linkUser;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("content")
        public String content;
        //        @SerializedName("year")
//        public String year;
        @SerializedName("month")
        public int month;
        @SerializedName("date")
        public String date;

        @Override
        public int getItemType() {
            return V3MoneyListBaseData.LIST_CONTENT;
        }
    }
}

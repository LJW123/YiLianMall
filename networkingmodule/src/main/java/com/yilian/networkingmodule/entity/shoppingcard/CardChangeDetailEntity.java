package com.yilian.networkingmodule.entity.shoppingcard;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/11/16 15:00
 * 购物卡变更明细
 */

public class CardChangeDetailEntity extends HttpResultBean {

    @SerializedName("statistics")
    public List<StatisticsBean> statistics;
    @SerializedName("list")
    public List<ListBean> list;

    public static class StatisticsBean {
        /**
         * expend : 20000
         * income : 0
         * date : 2018-07
         * year : 2018
         * month : 07
         */
        @SerializedName("expend")
        public String expend;
        @SerializedName("income")
        public String income;
        @SerializedName("date")
        public String date;
        @SerializedName("year")
        public String year;
        @SerializedName("month")
        public String month;
    }

    public static class ListBean {
        /**
         * id : 21
         * type : 101
         * amount : 1000
         * order : 2018072015421384671
         * time : 1532072533
         * merchant_uid : 0
         * link_user : 9253760342767600
         * type_msg : 京东商品订单支付
         * img : app/images/tubiao/jingdongshangpindingdanzhifu.png
         * year : 2018
         * month : 07
         * date : 2018-07
         */
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public int type;
        @SerializedName("amount")
        public String amount;
        @SerializedName("order")
        public String order;
        @SerializedName("time")
        public Long time;
        @SerializedName("merchant_uid")
        public String merchant_uid;
        @SerializedName("link_user")
        public String link_user;
        @SerializedName("type_msg")
        public String type_msg;
        @SerializedName("img")
        public String img;
        @SerializedName("year")
        public String year;
        @SerializedName("month")
        public String month;
        @SerializedName("date")
        public String date;
    }
}

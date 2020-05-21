package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by @author zhaiyaohua on 2018/2/13 0013.
 */

public class BeanEntity extends HttpResultBean {

    @SerializedName("statistics")
    public java.util.List<Statistics> statistics;
    @SerializedName("list")
    public java.util.List<DataList> list;

    public static class Statistics  extends BeansData{
        /**
         * income : 0
         * expend : 382688
         * date : 2018-02
         */
        @SerializedName("date")
        public String date;
    }

    public static class DataList extends BeansData{
        /**
         * id : 8
         * type : 101
         * amount : 155000
         * order : 2018021218241931923
         * time : 1518431059
         * img : app/images/tubiao/yidou.png
         * type_msg : 提现扣除
         * year : 2018
         * month : 02
         * date : 2018-02
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
        public long time;
        @SerializedName("img")
        public String img;
        @SerializedName("type_msg")
        public String typeMsg;
        @SerializedName("year")
        public String year;
        @SerializedName("month")
        public String month;
        @SerializedName("date")
        public String date;
    }
    public static  class BeansData{
        @SerializedName("income")
        public String income;
        @SerializedName("expend")
        public String expend;

    }
}

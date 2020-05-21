package com.yilian.networkingmodule.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * 兑换券收支明细
 *
 * @author Created by Zg on 2018/9/6.
 */

public class ExchangeTicketRecordEntity extends HttpResultBean {


    @SerializedName("list")
    public List<ListBean> list;
    @SerializedName("statistics")
    public List<StatisticsBean> statistics;

    public static class ListBean implements MultiItemEntity, Serializable {

        /**
         * id : 433
         * type : 101
         * amount : 8500
         * order : 2018072316062749816
         * time : 1532333192
         * merchant_uid : 6308797466188805
         * type_msg : 线上兑换商品
         * img : app/images/tubiao/xianshangduihuanshangping.png
         * year : 2018
         * month : 07
         * date : 2018-07
         */
        @SerializedName("link_user")
        public String userId;
        @SerializedName("link_phone")
        public String userPhone;
        @SerializedName("link_name")
        public String userName;
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
        @SerializedName("merchant_uid")
        public String merchantUid;
        @SerializedName("type_msg")
        public String typeMsg;
        @SerializedName("img")
        public String img;
        @SerializedName("year")
        public String year;
        @SerializedName("month")
        public String month;
        @SerializedName("date")
        public String date;
        public String expend;
        public String income;
        public int itemType;


        public ListBean() {

        }


        @Override
        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }


    }

    public static class StatisticsBean {
        /**
         * expend : 9700
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
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/17 0017.
 */

public class GALRecordEntity extends HttpResultBean {

    @SerializedName("count")
    public String count;
    @SerializedName("prize_num")
    public String prizeNum;
    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        public int position;
        @SerializedName("id")
        public String id;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("user_photo")
        public String userPhoto;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("countReply")
        public String countReply;
        @SerializedName("countPraise")
        public int countPraise;
        @SerializedName("is_parise")
        public String isParise;
        @SerializedName("considerations")
        public String considerations;
        @SerializedName("gain_percent")
        public String gainPercent;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_market_price")
        public String goodsMarketPrice;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("comment_index")
        public String commentIndex;

        @Override
        public String toString() {
            return "ListBean{" +
                    "orderIndex='" + orderId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userPhoto='" + userPhoto + '\'' +
                    ", joinCount='" + joinCount + '\'' +
                    ", goodsPrice='" + goodsPrice + '\'' +
                    ", countReply='" + countReply + '\'' +
                    ", countPraise='" + countPraise + '\'' +
                    ", isParise='" + isParise + '\'' +
                    ", considerations='" + considerations + '\'' +
                    ", gainPercent='" + gainPercent + '\'' +
                    ", goodsIcon='" + goodsIcon + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsMarketPrice='" + goodsMarketPrice + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    '}';
        }
    }
}

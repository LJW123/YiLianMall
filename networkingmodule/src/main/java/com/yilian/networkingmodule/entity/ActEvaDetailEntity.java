package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActEvaDetailEntity extends HttpResultBean {

    @SerializedName("info")
    public InfoBean info;

    public class InfoBean {
        @SerializedName("comment_index")
        public String comment_index;
        @SerializedName("comment_consumer")
        public String comment_consumer;
        @SerializedName("user_photo")
        public String user_photo;
        @SerializedName("comment_score")
        public String comment_score;
        @SerializedName("comment_content")
        public String comment_content;
        @SerializedName("comment_images")
        public String comment_images;
        @SerializedName("comment_goods_id")
        public String comment_goods_id;
        @SerializedName("comment_goods_sku")
        public String comment_goods_sku;
        @SerializedName("comment_time")
        public String comment_time;
        @SerializedName("goods_name")
        public String goods_name;
        @SerializedName("countReply")
        public String countReply;
        @SerializedName("is_parise")
        public String is_parise;
        @SerializedName("countPraise")
        public int countPraise;

        @Override
        public String toString() {
            return "InfoBean{" +
                    "comment_index='" + comment_index + '\'' +
                    ", comment_consumer='" + comment_consumer + '\'' +
                    ", user_photo='" + user_photo + '\'' +
                    ", comment_score='" + comment_score + '\'' +
                    ", comment_content='" + comment_content + '\'' +
                    ", comment_images='" + comment_images + '\'' +
                    ", comment_goods_id='" + comment_goods_id + '\'' +
                    ", comment_goods_sku='" + comment_goods_sku + '\'' +
                    ", comment_time='" + comment_time + '\'' +
                    ", goods_name='" + goods_name + '\'' +
                    ", countReply='" + countReply + '\'' +
                    ", is_parise='" + is_parise + '\'' +
                    ", countPraise='" + countPraise + '\'' +
                    '}';
        }
    }


    @SerializedName("reply_list")
    public ArrayList<ListBean> reply_list;

    public class ListBean {
        public int position;
        @SerializedName("reply_index")
        public String reply_index;
        @SerializedName("replyer_username")
        public String replyer_username;
        @SerializedName("user_photo")
        public String user_photo;
        @SerializedName("reply_content")
        public String reply_content;
        @SerializedName("replyer_time")
        public String replyer_time;
        @SerializedName("is_parise")
        public String is_parise;
        @SerializedName("countPraise")
        public int countPraise;
    }

}

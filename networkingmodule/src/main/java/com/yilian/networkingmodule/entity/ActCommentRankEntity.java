package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ZYH on 2017/12/15 0015.
 */

public class ActCommentRankEntity extends HttpResultBean {
    @SerializedName("list")
    public java.util.List<Comment> list;

    public static class Comment {
        /**
         * name : 乐分网
         * photo : app/images/head/20171109/7435908194499807_9657332_.jpg
         * comment_id : 1
         * comment_content : 哈哈哈哈
         * create_at : 1510361212
         * is_praise : 1
         * praise_num : 1
         */
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("comment_id")
        public String commentId;
        @SerializedName("comment_content")
        public String commentContent;
        @SerializedName("create_at")
        public String createAt;
        @SerializedName("is_praise")
        public int isPraise;
        @SerializedName("praise_num")
        public int praiseNum;
    }
}

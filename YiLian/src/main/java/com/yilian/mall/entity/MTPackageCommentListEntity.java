package com.yilian.mall.entity;/**
 * Created by  on 2016/12/7 0007.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2016/12/7 0007.
 * 套餐评论列表实体类 包括头部整体评论和下方列表评论内容
 */
public class MTPackageCommentListEntity extends BaseEntity {

    /**
     * tags : [{"tag_name":"","tag_count":""},{}]
     * count : 0
     * evaluate : null
     * data : [{"name":"","image_url":"","evaluate":"","commit_date":"","taste":"","environment":"","service":"","consumer_comment":"","merchant_comment":"","album":["",""]}]
     */

    @SerializedName("count")
    public String count;
    @SerializedName("img_count")
    public String imgCount;
    @SerializedName("evaluate")
    public String evaluate;//总评分
    @SerializedName("tags")
    public ArrayList<TagsBean> tags;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class TagsBean {
        /**
         * tag_name :
         * tag_count :
         */

        @SerializedName("tags_name")
        public String tagName;
        @SerializedName("tags_count")
        public String tagCount;
        @SerializedName("score")
        public String score;

        @Override
        public String toString() {
            return "TagsBean{" +
                    "tagName='" + tagName + '\'' +
                    ", tagCount='" + tagCount + '\'' +
                    ", score='" + score + '\'' +
                    '}';
        }
    }

    public static class DataBean {
        /**
         * name :
         * image_url :
         * evaluate :
         * commit_date :
         * taste :
         * environment :
         * service :
         * consumer_comment :
         * merchant_comment :
         * album : ["",""]
         */

        @SerializedName("name")
        public String name;
        @SerializedName("image_url")
        public String imageUrl;
        @SerializedName("evaluate")
        public String evaluate;
        @SerializedName("commit_date")
        public String commitDate;
        @SerializedName("taste")
        public String taste;
        @SerializedName("environment")
        public String environment;
        @SerializedName("service")
        public String service;
        @SerializedName("consumer_comment")
        public String consumerComment;
        @SerializedName("merchant_comment")
        public String merchantComment;
        @SerializedName("album")
        public String album;
    }
}

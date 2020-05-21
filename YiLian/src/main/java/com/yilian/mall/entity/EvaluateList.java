package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lauyk on 16/3/22.
 */
public class EvaluateList extends BaseEntity {

    public ArrayList<Evaluate> list;

    @SerializedName("total_score")
    public float totalScore;

    @SerializedName("total_count")
    public String totalCount;

    public class Evaluate {


        @SerializedName("comment_index")//评价序列编号
        public String commentIndex;
        @SerializedName("comment_consumer")//用户昵称
        public String commentConsumer;
        @SerializedName("icon")//用户头像
        public String icon;
        @SerializedName("comment_score")//评分
        public float commentScore;
        @SerializedName("comment_content")//评价内容
        public String commentContent;
        @SerializedName("comment_images")//评价图片
        public String commentImages;
        @SerializedName("comment_goods_id")//评价商品id
        public String commentGoodsId;
        @SerializedName("comment_goods_sku")//评价商品sku
        public String commentGoodsSku;
        @SerializedName("comment_praise")//点赞次数
        public String commentPraise;
        @SerializedName("comment_time")//评价时间
        public String commentTime;

    }

}

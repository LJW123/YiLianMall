package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActGoodsEvaListEntity extends HttpResultBean {

    @SerializedName("total_score")
    public String totalScore;
    @SerializedName("total_count")
    public String totalCount;


    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("comment_index")
        public String commentIndex;
        @SerializedName("comment_consumer")
        public String commentConsumer;
        @SerializedName("user_photo")
        public String userPhoto;
        @SerializedName("comment_score")
        public float commentScore;
        @SerializedName("comment_content")
        public String commentContent;
        @SerializedName("comment_images")
        public String commentImages;
        @SerializedName("comment_goods_id")
        public String commentGoodsId;
        @SerializedName("comment_goods_sku")
        public String commentGoodsSku;
        @SerializedName("comment_praise")
        public int commentPraise;
        @SerializedName("comment_time")
        public String commentTime;
        @SerializedName("order_time")
        public String orderTime;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("countReply")
        public String countReply;
        @SerializedName("is_parise")
        public String isParise;
    }

}

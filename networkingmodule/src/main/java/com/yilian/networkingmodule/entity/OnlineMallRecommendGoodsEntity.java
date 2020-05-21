package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 *  @author zhaiyaohua on 2018/1/4 0004.
 */

public class OnlineMallRecommendGoodsEntity extends HttpResultBean {


    @SerializedName("data")
    public List<Data> data;

    public static class Data extends OnlineMallCategoryData {
        /**
         * goods_id : 1970
         * goods_name : 一二三
         * supplier_id : 92
         * sell_count : 33
         * goods_status : 2
         * goods_price : 699900
         * goods_cost : 699900
         * goods_retail : 600000
         * goods_integral : 0
         * image_url : https://img.lefenmall.net/supplier/images/fe38bc54338dd70feb645dc7db688f8b90e73436_QQ图片20170516173343.jpg?x-oss-process=image/resize,limit_0,w_300,h_300,m_fill
         * goods_online : 1514441027
         * return_integral : 499500
         * tags_name :
         * type : 0
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("sell_count")
        public String sellCount;
        @SerializedName("goods_status")
        public String goodsStatus;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_retail")
        public String goodsRetail;
        @SerializedName("goods_integral")
        public String goodsIntegral;
        @SerializedName("image_url")
        public String imageUrl;
        @SerializedName("goods_online")
        public String goodsOnline;
        @SerializedName("return_integral")
        public int returnIntegral;
        @SerializedName("tags_name")
        public String tagsName;
        @SerializedName("type")
        public int type;

        @Override
        public int getItemType() {
            return OnlineMallCategoryData.ITEM_TYPE_WEIT_TWO;
        }

        @Override
        public int getSpanSize() {
            return OnlineMallCategoryData.ITEM_SPAN_SIZE_TWO;
        }
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/3/23 0023.
 * 显示抢购实体类
 */
public class FlashSaleEntity extends BaseEntity {

    /**
     * data : {"goods":[{"goods_id":"","goods_name":"","price":"","merchant_id":"","goods_num":"","buy_time":"","end_time":"","image_url":"","introduction":""},{}]}
     */
    @SerializedName("click")
    public String click;

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("goods")
        public ArrayList<GoodsBean> goods;

        @SerializedName("time")
        public String time;

        public class GoodsBean {
            /**
             * goods_id :
             * goods_name :
             * price :
             * merchant_id :
             * merchant_name :
             * goods_num :
             * total_num :
             * buy_time :
             * end_time :
             * image_url :
             * introduction :
             */

            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("price")
            public String price;
            @SerializedName("merchant_id")
            public String merchantId;
            @SerializedName("goods_num")
            public String goodsNum;
            @SerializedName("buy_time")
            public String buyTime;
            @SerializedName("introduction")
            public String introduction;
            @SerializedName("merchant_name")
            public String merchantName;
            @SerializedName("image_url")
            public String imageUrl;
            @SerializedName("end_time")
            public String endTime;
            @SerializedName("total_num")
            public String totalNum;
            @SerializedName("belong")
            public String belong;//商品归属 0本地商家 1总部旗舰店
            @SerializedName("goods_label")
            public String goodsLabel;//商品标签 空字符串不显示
            @SerializedName("goods_type")
            public String goodsType;//本地显示购是否支持配送
            @SerializedName("fregiht_price")
            public String fregihtPrice;//运费
        }
    }
}

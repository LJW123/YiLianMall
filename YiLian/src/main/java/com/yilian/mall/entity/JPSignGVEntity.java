package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/10/28 0028.
 */

public class JPSignGVEntity extends BaseEntity{

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * goods_id : 1
         * goods_name : 乐分定制 易得康洗衣液
         * sell_count : 0
         * image_url : uploads/goods/goods_info/20151116/20151116165115_751246.jpg
         * goods_price : 1000
         * goods_cost : 450
         * supplier_id : 0
         * tags_id : 0
         * tags_name :
         */

        @SerializedName("goods_list")
        public ArrayList<JPGoodsEntity> goodsList;

    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2016/10/18 0018.
 */

public class JPSubClassfiyGoodsEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * goods_name :
         * filiale_id :
         * supplier_id :
         * goods_id :
         * tags_name :
         * tags_id :
         * goods_price :
         * goods_cost :
         * sell_count :
         * image_url :
         * goods_online :
         */

        @SerializedName("goods")
        public ArrayList<JPGoodsEntity> goods;

    }
}

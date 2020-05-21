package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2016/10/29 0029.
 */

public class JPMainFragmentGoodEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean JPShopData;

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
         */

        @SerializedName("goods")
        public ArrayList<JPGoodsEntity> JPShopGoods;

    }
}

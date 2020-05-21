package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2016/10/25 0025.
 */

public class JPFragmentGoodEntity extends BaseEntity {

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

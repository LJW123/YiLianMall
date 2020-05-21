package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 */

public class FlagshipList extends BaseEntity{

    /**
     * image_url :
     * suppliers : [{"supplier_name":"","supplier_id":"","tags_name":"","image_url":""},{}]
     * goods : [{" goods_name":""," goods_icon":"","goods_id":"","goods_sale":"","goods_price":"","goods_cost":""},{}]
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("image_url")
        public String imageUrl;
        /**
         * supplier_name :
         * supplier_id :
         * tags_name :
         * image_url :
         */

        @SerializedName("suppliers")
        public List<SuppliersBean> suppliers;

        public class SuppliersBean {
            @SerializedName("supplier_name")
            public String supplierName;
            @SerializedName("supplier_id")
            public String supplierId;
            @SerializedName("tags_name")
            public String tagsName;
            @SerializedName("tags_id")
            public String tagId;
            @SerializedName("image_url")
            public String imageUrl;
        }
    }



}

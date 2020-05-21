package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by  on 2016/10/24 0024.
 * 旗舰店 实体类
 */

public class JPShopEntity extends BaseEntity {


    /**
     * image_url :
     * suppliers : [{"supplier_name":"","supplier_id":"","tags_name":"","image_url":""},{}]
     * goods : [{"goods_name":"","goods_icon":"","goods_id":"","goods_sale":"","goods_price":"","goods_cost":""},{}]
     */

    @SerializedName("data")
    public DataBean JPShopData;


    public class DataBean {
//        多余的字段
//        @SerializedName("image_url")
//        public String JPShopImageUrl;
        /**
         * supplier_name :
         * supplier_id :
         * tags_name :
         * image_url :
         */

        @SerializedName("suppliers")
        public ArrayList<SuppliersBean> JPShopSuppliers;
        /**
         * goods_name :
         * goods_icon :
         * goods_id :
         * goods_sale :
         * goods_price :
         * goods_cost :
         */

        @SerializedName("goods")
        public ArrayList<GoodsBean> JPShopGoods;

        public class SuppliersBean extends MainCategoryData{
            @SerializedName("supplier_name")
            public String JPShopSupplierName;
            @SerializedName("supplier_id")
            public String JPShopSupplierId;
            @SerializedName("tags_name")
            public String JPShopTagsName;
            @SerializedName("image_url")
            public String JPShopImageUrl;
            @SerializedName("tags_id")
            public String JPShopTagsId;

            @Override
            public int getItemType() {
                return MainCategoryData.SHOP;
            }

            @Override
            public int getSpanSize() {
                return MainCategoryData.ITEM_SIZE;
            }
        }

        public class GoodsBean {
            @SerializedName("goods_name")
            public String JPShopGoodsName;
            @SerializedName("goods_icon")
            public String JPShopGoodsIcon;
            @SerializedName("goods_id")
            public String JPShopGoodsId;
            @SerializedName("goods_sale")
            public String JPShopGoodsSale;
            @SerializedName("goods_price")
            public String JPShopGoodsPrice;
            @SerializedName("goods_cost")
            public String JPShopGoodsCost;
        }
    }
}

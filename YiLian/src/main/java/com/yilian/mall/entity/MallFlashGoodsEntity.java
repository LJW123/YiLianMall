package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/4/18 0018.
 */
public class MallFlashGoodsEntity extends com.yilian.networkingmodule.entity.BaseEntity {

    /**
     * data : {"info":{"desc_tags":["品质保障","精选品牌","快速配送"],"goods_album":["goods/147831263659852.jpg"],"goods_brand":"","goods_grade":50,"goods_id":"1197","goods_introduce":["uploads/goods/introduce/20151014/20151014145337_710569.jpg","uploads/goods/introduce/20151014/20151014145337_148920.jpg","uploads/goods/introduce/20151014/20151014145338_27931.jpg","uploads/goods/introduce/20151014/20151014145339_623291.jpg","uploads/goods/introduce/20151014/20151014145339_340767.jpg","uploads/goods/introduce/20151014/20151014145340_208242.jpg"],"goods_region":"0","goods_sku":[],"goods_supplier":"317","id":"1","market_price":"10000","name":"你不可不买的新款运动休闲鞋_2016新款运动休闲鞋男士跑步鞋户外潮流男鞋软底真皮","overplus":"19","start_at":"1492567200","supplier_freight_desc":"213","tags_msg":"特惠","time":1492654846,"total":"20","voucher_price":"9900"}}
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * info : {"desc_tags":["品质保障","精选品牌","快速配送"],"goods_album":["goods/147831263659852.jpg"],"goods_brand":"","goods_grade":50,"goods_id":"1197","goods_introduce":["uploads/goods/introduce/20151014/20151014145337_710569.jpg","uploads/goods/introduce/20151014/20151014145337_148920.jpg","uploads/goods/introduce/20151014/20151014145338_27931.jpg","uploads/goods/introduce/20151014/20151014145339_623291.jpg","uploads/goods/introduce/20151014/20151014145339_340767.jpg","uploads/goods/introduce/20151014/20151014145340_208242.jpg"],"goods_region":"0","goods_sku":[],"goods_supplier":"317","id":"1","market_price":"10000","name":"你不可不买的新款运动休闲鞋_2016新款运动休闲鞋男士跑步鞋户外潮流男鞋软底真皮","overplus":"19","start_at":"1492567200","supplier_freight_desc":"213","tags_msg":"特惠","time":1492654846,"total":"20","voucher_price":"9900"}
         */

        @SerializedName("info")
        public InfoBean info;

        public class InfoBean {
            /**
             * desc_tags : ["品质保障","精选品牌","快速配送"]
             * goods_album : ["goods/147831263659852.jpg"]
             * goods_brand :
             * goods_grade : 50
             * goods_id : 1197
             * goods_introduce : ["uploads/goods/introduce/20151014/20151014145337_710569.jpg","uploads/goods/introduce/20151014/20151014145337_148920.jpg","uploads/goods/introduce/20151014/20151014145338_27931.jpg","uploads/goods/introduce/20151014/20151014145339_623291.jpg","uploads/goods/introduce/20151014/20151014145339_340767.jpg","uploads/goods/introduce/20151014/20151014145340_208242.jpg"]
             * goods_region : 0
             * goods_sku : []
             * goods_supplier : 317
             * id : 1
             * market_price : 10000
             * name : 你不可不买的新款运动休闲鞋_2016新款运动休闲鞋男士跑步鞋户外潮流男鞋软底真皮
             * overplus : 19
             * start_at : 1492567200
             * supplier_freight_desc : 213
             * tags_msg : 特惠
             * time : 1492654846
             * total : 20
             * voucher_price : 9900
             */

            @SerializedName("goods_brand")
            public String goodsBrand;
            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("goods_region")
            public String goodsRegion;
            @SerializedName("goods_supplier")
            public String goodsSupplier;
            @SerializedName("supplier_id")
            public String supplierId;
            @SerializedName("id")
            public String id;
            @SerializedName("market_price")
            public String marketPrice;
            @SerializedName("name")
            public String name;
            @SerializedName("overplus")
            public String overplus;
            @SerializedName("start_at")
            public String startAt;
            @SerializedName("supplier_freight_desc")
            public String supplierFreightDesc;
            @SerializedName("supplier_address")
            public String supplier_address;
            @SerializedName("tags_msg")
            public String tagsMsg;
            @SerializedName("time")
            public int time;
            @SerializedName("total")
            public String total;
            @SerializedName("voucher_price")
            public String voucherPrice;
            @SerializedName("goods_freight_desc")
            public String goodsFreightDesc;
            @SerializedName("goods_grade")
            public String goodsGrade;
            @SerializedName("desc_tags")
            public List<String> descTags;
            @SerializedName("goods_album")
            public List<String> goodsAlbum;
            @SerializedName("goods_introduce")
            public List<String> goodsIntroduce;
            @SerializedName("pic_id")
            public String picId;
            @SerializedName("sku")
            public List<String> goodsSku;
            @SerializedName("goods_type")
            public String goods_type;
            @SerializedName("goods_grade_count")
            public String goods_grade_count;
        }
    }
}

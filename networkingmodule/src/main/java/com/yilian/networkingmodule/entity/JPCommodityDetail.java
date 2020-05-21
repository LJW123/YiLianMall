package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by Ray_L_Pain on 2016/10/24 0024.
 */

public class JPCommodityDetail extends HttpResultBean {


    /**
     * good_info : {"goods_id":"7","goods_name":"易得康 苹果味洗手晶","goods_brand":"易得康","goods_price":"4800","goods_type":"3","goods_online":"1460369715","goods_grade":40,"goods_grade_count":"1","goods_sale":"63","goods_supplier_recommend":"0","goods_cost":"0","goods_renqi":"252","goods_video":"","goods_discount":"10","goods_album":["uploads/goods/goods_info/20160131/20160131105705_357223.jpg"],"goods_introduce":["uploads/goods/introduce/20160425/20160425155521_513255.jpg","uploads/goods/introduce/20160425/20160425155522_880874.jpg","uploads/goods/introduce/20160425/20160425155522_463120.jpg","uploads/goods/introduce/20160425/20160425155524_902359.jpg","uploads/goods/introduce/20160425/20160425155525_948224.jpg","uploads/goods/introduce/20160425/20160425155525_319113.jpg","uploads/goods/introduce/20160425/20160425155527_499905.jpg","uploads/goods/introduce/20160425/20160425155529_754579.jpg"],"GoodsSkuPriceBean":[{"sku_goods":"3202","sku_info":"0:0","sku_market_price":"4800","sku_integral_price":"48","sku_integral_money":"0"}],"goods_sku_property":[],"goods_sku_values":[],"supplier_info":{"supplier_id":"0","supplier_tags":"","supplier_name":"","supplier_address":"","goods_freight_desc":""},"desc_tags":["品质保障","7天退货","精选品牌","成本价"],"login_status":0,"yet_collect":0}
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * goods_id : 7
         * goods_name : 易得康 苹果味洗手晶
         * goods_brand : 易得康
         * goods_price : 4800
         * goods_type : 3乐分（显示两个） 4乐享（显示三个）
         * goods_online : 1460369715
         * goods_grade : 40
         * goods_grade_count : 1
         * goods_sale : 63
         * goods_supplier_recommend : 0
         * goods_cost : 0
         * goods_renqi : 252
         * goods_video :
         * goods_discount : 10
         * goods_album : ["uploads/goods/goods_info/20160131/20160131105705_357223.jpg"]
         * goods_introduce : ["uploads/goods/introduce/20160425/20160425155521_513255.jpg","uploads/goods/introduce/20160425/20160425155522_880874.jpg","uploads/goods/introduce/20160425/20160425155522_463120.jpg","uploads/goods/introduce/20160425/20160425155524_902359.jpg","uploads/goods/introduce/20160425/20160425155525_948224.jpg","uploads/goods/introduce/20160425/20160425155525_319113.jpg","uploads/goods/introduce/20160425/20160425155527_499905.jpg","uploads/goods/introduce/20160425/20160425155529_754579.jpg"]
         * GoodsSkuPriceBean : [{"sku_goods":"3202","sku_info":"0:0","sku_market_price":"4800","sku_integral_price":"48","sku_integral_money":"0"}]
         * goods_sku_property : []
         * goods_sku_values : []
         * supplier_info : {"supplier_id":"0","supplier_tags":"","supplier_name":"","supplier_address":"","goods_freight_desc":""}
         * desc_tags : ["品质保障","7天退货","精选品牌","成本价"]
         * login_status : 0
         * yet_collect : 0
         */

        @SerializedName("good_info")
        public GoodInfoBean goodInfo;

        public class GoodInfoBean {
            /**
             * 平台赠送益豆数量 需要除以100
             */
            @SerializedName("subsidy")
            public float subsidy;
            @SerializedName("goods_unit")
            public String goodsUnit;
            @SerializedName("return_integral")
            public String returnIntegral;
            @SerializedName("tags_msg")
            public String tagsMsg;
            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("goods_brand")
            public String goodsBrand;
            @SerializedName("goods_price")
            public String goodsPrice;
            @SerializedName("goods_region")
            public String goodsRegion;
            @SerializedName("goods_type")
            public String goodsType;
            @SerializedName("goods_online")
            public String goodsOnline;
            @SerializedName("goods_grade")
            public float goodsGrade;
            @SerializedName("goods_grade_count")
            public String goodsGradeCount;
            @SerializedName("goods_sale")
            public String goodsSale;
            @SerializedName("goods_supplier")
            public String goodsSupplier;
            @SerializedName("goods_filiale")
            public String goodsFiliale;
            @SerializedName("goods_cost")
            public String goodsCost;
            @SerializedName("return_bean")
            public String returnBean;
            @SerializedName("goods_renqi")
            public String goodsRenqi;
            @SerializedName("goods_video")
            public String goodsVideo;
            @SerializedName("goods_discount")
            public String goodsDiscount;
            @SerializedName("goods_sku")
            public String goodsSku;
            @SerializedName("goods_sku_count")
            public String goodsSkuCount;
            @SerializedName("goods_attach") //status = 5时的购买须知
            public String goodsAttach;

            @SerializedName("goods_integral")
            public String goodsIntegral;//易划算的奖券价格
            @SerializedName("retail_price")
            public String retailPrice;//奖券购的奖券市场价

            /**
             * supplier_id : 0
             * supplier_tags :
             * supplier_name :
             * supplier_address :
             * goods_freight_desc :
             */

            @SerializedName("supplier_info")
            public SupplierInfoBean supplierInfo;
            @SerializedName("login_status")
            public int loginStatus;
            @SerializedName("yet_collect")
            public int yetCollect;
            @SerializedName("goods_album")
            public List<String> goodsAlbum;
            @SerializedName("goods_introduce")
            public List<String> goodsIntroduce;
            /**
             * sku_goods : 3202
             * sku_info : 0:0
             * sku_market_price : 4800
             * sku_integral_price : 48
             * sku_integral_money : 0
             */

            @SerializedName("goods_sku_price")
            public List<GoodsSkuPrice> goodsSkuPrice;
            @SerializedName("goods_sku_property")
            public List<GoodsSkuProperty> goodsSkuProperty;
            @SerializedName("goods_sku_values")
            public List<GoodsSkuProperty> goodsSkuValues;
            @SerializedName("desc_tags")
            public List<String> descTags;

            @SerializedName("type")
            public String type;

            @Override
            public String toString() {
                return "GoodInfoBean{" +
                        "goods_id='" + goodsId + '\'' +
                        ", goodsName='" + goodsName + '\'' +
                        ", goodsBrand='" + goodsBrand + '\'' +
                        ", goodsPrice='" + goodsPrice + '\'' +
                        ", goodsRegion='" + goodsRegion + '\'' +
                        ", goodsType='" + goodsType + '\'' +
                        ", goodsOnline='" + goodsOnline + '\'' +
                        ", goodsGrade=" + goodsGrade +
                        ", goodsGradeCount='" + goodsGradeCount + '\'' +
                        ", goodsSale='" + goodsSale + '\'' +
                        ", goodsSupplier='" + goodsSupplier + '\'' +
                        ", goodsCost='" + goodsCost + '\'' +
                        ", goodsRenqi='" + goodsRenqi + '\'' +
                        ", goodsVideo='" + goodsVideo + '\'' +
                        ", goodsDiscount='" + goodsDiscount + '\'' +
                        ", supplierInfo=" + supplierInfo +
                        ", loginStatus=" + loginStatus +
                        ", yetCollect=" + yetCollect +
                        ", goodsAlbum=" + goodsAlbum +
                        ", goodsIntroduce=" + goodsIntroduce +
                        ", goodsSkuPrice=" + goodsSkuPrice +
                        ", goodsSkuProperty=" + goodsSkuProperty +
                        ", goodsSkuValues=" + goodsSkuValues +
                        ", descTags=" + descTags +
                        '}';
            }

            public class SupplierInfoBean {
                @SerializedName("supplier_id")
                public String supplierId;
                @SerializedName("supplier_tags")
                public String supplierTags;
                @SerializedName("supplier_name")
                public String supplierName;
                @SerializedName("supplier_address")
                public String supplierAddress;
                @SerializedName("goods_freight_desc")
                public String goodsFreightDesc;
            }

//            public static class GoodsSkuPriceBean {
//                @SerializedName("sku_goods")
//                public String skuGoods;
//                @SerializedName("sku_info")
//                public String skuInfo;
//                @SerializedName("sku_market_price")
//                public String skuMarketPrice;
//                @SerializedName("sku_integral_price")
//                public String skuIntegralPrice;
//                @SerializedName("sku_integral_money")
//                public String skuIntegralMoney;
//            }
        }
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *猜价格-碰运气活动详情实体类
 * @author Ray_L_Pain
 * @date 2016/10/24 0024
 */

public class ActivityDetailEntity extends BaseEntity{


    @SerializedName("data")
    public DataBean data;

    public class DataBean {

        @SerializedName("good_info")
        public GoodInfoBean goodInfo;

        public class GoodInfoBean {
            @Override
            public String toString() {
                return "GoodInfoBean{" +
                        "integral='" + integral + '\'' +
                        ", actId='" + actId + '\'' +
                        ", actType='" + actType + '\'' +
                        ", prizeNum='" + prizeNum + '\'' +
                        ", guessNum='" + guessNum + '\'' +
                        ", returnIntegral='" + returnIntegral + '\'' +
                        ", tagsMsg='" + tagsMsg + '\'' +
                        ", goodsId='" + goodsId + '\'' +
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
                        ", goodsFiliale='" + goodsFiliale + '\'' +
                        ", goodsCost='" + goodsCost + '\'' +
                        ", goodsRenqi='" + goodsRenqi + '\'' +
                        ", goodsVideo='" + goodsVideo + '\'' +
                        ", goodsDiscount='" + goodsDiscount + '\'' +
                        ", goodsSku='" + goodsSku + '\'' +
                        ", goodsSkuCount='" + goodsSkuCount + '\'' +
                        ", goodsAttach='" + goodsAttach + '\'' +
                        ", goodsIntegral='" + goodsIntegral + '\'' +
                        ", retailPrice='" + retailPrice + '\'' +
                        ", guess_price_gain_percent=" + guess_price_gain_percent +
                        ", try_luck_gain_percent=" + try_luck_gain_percent +
                        ", supplierInfo=" + supplierInfo +
                        ", loginStatus=" + loginStatus +
                        ", yetCollect=" + yetCollect +
                        ", goodsAlbum=" + goodsAlbum +
                        ", goodsIntroduce=" + goodsIntroduce +
                        ", goodsSkuPrice=" + goodsSkuPrice +
                        ", goodsSkuProperty=" + goodsSkuProperty +
                        ", goodsSkuValues=" + goodsSkuValues +
                        ", descTags=" + descTags +
                        ", type='" + type + '\'' +
                        '}';
            }

            @SerializedName("integral")
            public String integral;
            @SerializedName("act_id")
            public String actId;
            @SerializedName("act_type")
            public String actType;
            @SerializedName("prize_num")
            public String prizeNum;
            @SerializedName("guessNum")
            public String guessNum;

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

            @SerializedName("guess_price_gain_percent")
            public float guess_price_gain_percent;
            @SerializedName("try_luck_gain_percent")
            public float try_luck_gain_percent;

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

        }
    }
}

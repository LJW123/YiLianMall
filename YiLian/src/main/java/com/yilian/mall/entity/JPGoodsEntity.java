package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by  on 2016/10/18 0018.
 * 新版商城 商品 实体类
 */

public class JPGoodsEntity implements Serializable {

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
     * goods_online:上线时间
     * hasGoods: 是否有货 0无货 1有货
     * subsidy_show:是否显示加赠益豆 1--显示 其他不显示
     * subsidy：平台加赠的益豆
     * return_bean:赠送的益豆
     */
    @SerializedName("subsidy")
    public String subsidy;
    @SerializedName("return_bean")
    public String returnBean;
    @SerializedName("goods_name")
    public String JPGoodsName;
    @SerializedName("filiale_id")
    public String JPFilialeId;
    @SerializedName("supplier_id")
    public String JPSupplierId;
    @SerializedName("goods_id")
    public String JPGoodsId;
    @SerializedName("tags_name")
    public String JPTagsName;
    @SerializedName("tags_id")
    public String JPTagsId;
    @SerializedName("goods_price")
    public String JPGoodsPrice;
    @SerializedName("goods_cost")
    public String JPGoodsCost;
    @SerializedName("sell_count")
    public String JPSellCount;
    @SerializedName("image_url")
    public String JPImageUrl;
    @SerializedName("goods_online")
    public String goodsOnline;
    @SerializedName("has_goods")
    public String hasGoods;
    @SerializedName("return_integral")
    public String returnIntegral;//销售奖券

    @SerializedName("goods_type")
    public String goodsType;
    @SerializedName("goods_integral")//奖券价格
    public String goodsIntegral;
    @SerializedName("type")
    public String type;//0普通商品 1易划算 2奖券购

    @Override
    public String toString() {
        return "JPGoodsEntity{" +
                "JPGoodsName='" + JPGoodsName + '\'' +
                ", JPFilialeId='" + JPFilialeId + '\'' +
                ", JPSupplierId='" + JPSupplierId + '\'' +
                ", JPGoodsId='" + JPGoodsId + '\'' +
                ", JPTagsName='" + JPTagsName + '\'' +
                ", JPTagsId='" + JPTagsId + '\'' +
                ", JPGoodsPrice='" + JPGoodsPrice + '\'' +
                ", JPGoodsCost='" + JPGoodsCost + '\'' +
                ", JPSellCount='" + JPSellCount + '\'' +
                ", JPImageUrl='" + JPImageUrl + '\'' +
                ", goodsOnline='" + goodsOnline + '\'' +
                ", hasGoods='" + hasGoods + '\'' +
                ", returnIntegral='" + returnIntegral + '\'' +
                '}';
    }
}

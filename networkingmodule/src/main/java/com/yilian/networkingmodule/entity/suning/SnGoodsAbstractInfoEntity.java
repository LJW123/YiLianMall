package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by zhaiyaohua on 2018/5/22.
 *         苏宁商品摘要信息
 */

public class SnGoodsAbstractInfoEntity {
    /**
     * 商品唯一标示
     */
    @SerializedName("skuId")
    public String skuId;
    /**
     * 商品名称
     */
    @SerializedName("name")
    public String name;
    /**
     * 商品缩略图
     */
    @SerializedName("image")
    public String image;
    /**
     * 价格
     */
    @SerializedName("snPrice")
    public float snPrice;
    /**
     * 销量
     */
    @SerializedName("sale")
    public int saleCount;
    /**
     * 赠送益豆
     */
    @SerializedName("return_bean")
    public float returnBean;


}

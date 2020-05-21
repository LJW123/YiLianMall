package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;

/**
 * @author Created by  on 2018/5/22.
 *         JD商品摘要信息
 */

public class JDGoodsAbstractInfoEntity  {
    /**
     * 商品唯一标示
     */
    @SerializedName("sku")
    public String skuId;
    /**
     * 商品名称
     */
    @SerializedName("name")
    public String name;
    /**
     * 商品缩略图
     */
    @SerializedName("imagePath")
    public String imagePath;
    /**
     * 价格
     */
    @SerializedName("jdPrice")
    public float jdPrice;
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

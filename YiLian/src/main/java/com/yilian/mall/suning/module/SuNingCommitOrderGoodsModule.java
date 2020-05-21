package com.yilian.mall.suning.module;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * @author xiaofo on 2018/7/19.
 */

public class SuNingCommitOrderGoodsModule implements Serializable {
    public SuNingCommitOrderGoodsModule(String skuId,int count, String goodsName,
                                        String price, String returnBean,
                                        String imgUrl,@Nullable String cartIndex) {
        this.skuId = skuId;
        this.count = count;
        this.goodsName = goodsName;
        this.price = price;
        this.returnBean = returnBean;
        this.imgUrl = imgUrl;

        this.cartIndex = cartIndex;
    }

    /**
     * 数量
     */
    public int count;
    /**
     * 商品名称
     */
    public String goodsName;
    /**
     * 商品价格
     */
    public String price;
    /**
     * 赠送益豆
     */
    public String returnBean;
    /**
     * 图片Url
     */
    public String imgUrl;
    /**
     * 购物车ID
     * 购物车里面的商品中的购物车ID
     */
    @Nullable
    public  String cartIndex;
    /**
     * 商品sku
     */
    public String skuId;
}

package com.yilian.networkingmodule.entity.jd;

import java.io.Serializable;

/**
 * @author xiaofo on 2018/6/27.
 */

public class JDCommitOrderEntity implements Serializable {
    public String goodsName;
    public String goodsSku;
    public String imagePath;
    public int goodsCount;
    public String goodsPrice;
    public String goodsJdPrice;
    public String returnBean;
    public String cartIndex;

    public JDCommitOrderEntity(String goodsName, String goodsSku, String imagePath,
                               int goodsCount, String goodsPrice, String goodsJdPrice,
                               String returnBean, String cartIndex) {
        this.goodsName = goodsName;
        this.goodsSku = goodsSku;
        this.imagePath = imagePath;
        this.goodsCount = goodsCount;
        this.goodsPrice = goodsPrice;
        this.goodsJdPrice = goodsJdPrice;
        this.returnBean = returnBean;
        this.cartIndex = cartIndex;
    }

    public JDCommitOrderEntity(String goodsName, String goodsSku, String imagePath,
                               int goodsCount, String goodsPrice, String goodsJdPrice,
                               String returnBean) {
        this.goodsName = goodsName;
        this.goodsSku = goodsSku;
        this.imagePath = imagePath;
        this.goodsCount = goodsCount;
        this.goodsPrice = goodsPrice;
        this.goodsJdPrice = goodsJdPrice;
        this.returnBean = returnBean;
    }
}

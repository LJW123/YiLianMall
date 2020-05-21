package com.yilian.mall.entity;

import java.io.Serializable;

public class OrderSubmitGoods implements Serializable {
    /**
     * 平台赠送益豆数量 需要除以100
     */
    public float subsidy;
    public String goodsId;

    public int goodsType;//3乐购区（送区）4买区 5零购券 6是限时抢购详情零购券

    public String goodsPic;

    public String goodsName;

    public String goodsNorms;//sku

    public String goodsProperty;

    public double goodsPrice;//商品价格 单价

    public double goodsCost;//商品成本价（总部商品和不能使用抵扣券时值默认为0） 单价

    public double goodsReturnIntegral;//销售奖券

    public double goodsReturnBean;

    public double coupon;//抵扣券（若不能使用抵扣券值为0） 单价

    public String goodsDiscountPrice;

    public int goodsCount;

    public String type;

    public String filiale_id;//商品归属兑换中心（兑换中心商品归属id,其他商品为0）

    public String region_id;//商品地区（总部为0，其他未地区id）

    public String supplier_id;//商品归属旗舰店（供货商商品归属id,其他商品为0）

    public String name;//商品的归属名字


    public String label;//商品的标签

    public String fregihtPrice;//运费

    public String sku;//商品规格

    public String activityId;//活动Id

    public String addressId;//收货地址Id

    public String payType;//支付的方式是全额支付或者拼团支付

    public double goods_integral;

    public String goods_type; // 0普通 1易划算 2奖券购

    public double retailPrice; // 奖券购商品 现金


    @Override
    public String toString() {
        return "OrderSubmitGoods{" +
                "goods_id='" + goodsId + '\'' +
                ", goodsType=" + goodsType +
                ", goodsPic='" + goodsPic + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsNorms='" + goodsNorms + '\'' +
                ", goodsProperty='" + goodsProperty + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsCost=" + goodsCost +
                ", coupon=" + coupon +
                ", goodsDiscountPrice='" + goodsDiscountPrice + '\'' +
                ", goodsCount=" + goodsCount +
                ", type='" + type + '\'' +
                ", filiale_id='" + filiale_id + '\'' +
                ", region_id='" + region_id + '\'' +
                ", supplier_id='" + supplier_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.yilian.mall.entity;

import java.io.Serializable;

/**
 * @author Ray_L_Pain
 * 活动商品提交订单的参数实体类
 */
public class ActivitySubmitGoods implements Serializable {

    public String userName;
    public String userPhone;
    public String location;
    public String addressId;//收货地址Id
    public String integral;
    public String goodsId;
    public String goodsPic;
    public String goodsName;
    public String supplierName;
    public String sku;//商品规格
    public String norms;//商品规格
    public String orderId;

    @Override
    public String toString() {
        return "ActivitySubmitGoods{" +
                "userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", location='" + location + '\'' +
                ", addressId='" + addressId + '\'' +
                ", integral='" + integral + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", goodsPic='" + goodsPic + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", sku='" + sku + '\'' +
                ", norms='" + norms + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}

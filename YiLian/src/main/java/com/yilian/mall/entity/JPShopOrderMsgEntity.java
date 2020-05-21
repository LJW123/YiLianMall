package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2016/12/27 0027.
 */

public class JPShopOrderMsgEntity {
    //购物车下单备注信息 [{"shop_id":"","remark":"","type":""},{}] type 0 (对应购物车列表shop_type=1)正常旗舰店，1(对应购物车列表shop_type=2)兑换中心id
    @SerializedName("shop_id")
    public String shopId;
    @SerializedName("remark")
    public String remark;
    @SerializedName("type")
    public String type;

    @Override
    public String toString() {
        return "{" +
                "shop_id:'" + shopId + '\'' +
                ", remark:'" + remark + '\'' +
                ", type:'" + type + '\'' +
                '}';
    }
}

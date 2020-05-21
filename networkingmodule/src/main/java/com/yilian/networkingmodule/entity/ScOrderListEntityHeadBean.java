package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/8/29 0029.
 */

public class ScOrderListEntityHeadBean extends BaseBarcodeEntity {
    public ScOrderListEntityHeadBean(String orderId, String phone) {
        this.orderId = orderId;
        this.phone = phone;
    }

    @SerializedName("order_id")
    public String orderId;
    @SerializedName("phone")
    public String phone;


    @Override
    public int getItemType() {
        return BaseBarcodeEntity.HEADVIEW;
    }
}

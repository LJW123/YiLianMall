package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/8/29 0029.
 */

public class ScOrderListEntityBottomBean extends BaseBarcodeEntity {
    public ScOrderListEntityBottomBean(String orderIndex, String orderGoodsPrice, long merchantIntegral, long userIntegral,
                                       String orderStatus, String totalCount, String orderSupplierPrice, String merchantType,
                                       String orderConsumer, String orderTotalCount,String phone) {
        this.orderIndex = orderIndex;
        this.orderGoodsPrice = orderGoodsPrice;
        this.merchantIntegral = merchantIntegral;
        this.userIntegral = userIntegral;
        this.orderStatus = orderStatus;
        this.totalCount = totalCount;
        this.orderSupplierPrice = orderSupplierPrice;
        this.merchantType = merchantType;
        this.orderConsumer = orderConsumer;
        this.orderTotalCount = orderTotalCount;
        this.phone=phone;
    }

    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("order_goods_price")
    public String orderGoodsPrice;
    @SerializedName("merchant_integral")
    public long merchantIntegral;
    @SerializedName("user_integral")
    public long userIntegral;
    @SerializedName("order_status")
    public String orderStatus;
    @SerializedName("total_count")
    public String totalCount;
    @SerializedName("order_supplier_price")
    public String orderSupplierPrice;
    @SerializedName("merchant_type")
    public String merchantType;
    @SerializedName("order_consumer")
    public String orderConsumer;
    @SerializedName("order_total_count")
    public String orderTotalCount;
    @SerializedName("phone")
    public String phone;//点击支付需要传地字段

    @Override
    public int getItemType() {
        return BaseBarcodeEntity.BOTTOMVIEW;
    }
}

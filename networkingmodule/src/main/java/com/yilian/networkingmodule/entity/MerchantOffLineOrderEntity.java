package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/7/12 0012.
 */

public class MerchantOffLineOrderEntity extends BaseEntity {
    /**
     * bonus : 10 //让利额（上缴总金额）
     * merchant_percent : 67  折扣
     * order_index : 4036
     * payment_fee : 33.00
     */

    @SerializedName("bonus")
    public double bonus;
    @SerializedName("merchant_percent")
    public String merchantPercent;
    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("payment_fee")
    public String paymentFee;

}

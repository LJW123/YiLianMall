package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/8/30 0030.
 */

public class MerchantMakeOrderEntity extends HttpResultBean {
    @SerializedName("total_goods_price")
    public String totalGoodsPrice;
    @SerializedName("list")
    public Order list;

    public class Order {
        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_name")
        public String orderName;
    }
}

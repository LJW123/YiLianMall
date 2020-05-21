package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuyuqi on 2016/12/9 0009.
 * 提交订单
 */
public class MTSubmitOrderEntity extends BaseEntity implements Serializable{


    /**
     * name :
     * count :
     * money :
     * order_id :
     */

    @SerializedName("name")
    public String name;
    @SerializedName("count")
    public String count;
    @SerializedName("money")
    public String money;
    @SerializedName("order_id")
    public String orderId;
}

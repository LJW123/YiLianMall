package com.yilian.mall.entity;
/**
 * Created by  on 2016/12/23 0023.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 套餐支付成功
 * Created by  on 2016/12/23 0023.
 */
public class MTPaySuccessEntity extends BaseEntity implements Serializable{


    /**
     * usable_time :
     * order_id :
     * name :
     * codes : [{"code":"10004900","status":"1"},{"code":"10008555","status":"1"}]
     */

    @SerializedName("usable_time")
    public String usableTime;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("name")
    public String packageName;
    @SerializedName("delivery_price")//订单运费
    public String deliveryPrice;
    @SerializedName("codes")
    public ArrayList<MTCodesEntity> codes;
    @SerializedName("is_delivery")
    public String isDelivery;//该订单是否是配送订单 1是 0不是
    @SerializedName("integral")
    public String integral;
}

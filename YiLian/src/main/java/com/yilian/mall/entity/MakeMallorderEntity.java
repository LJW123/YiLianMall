package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/26.
 * 从商品详情下订单时
 */
public class MakeMallorderEntity extends BaseEntity implements Serializable {

    @SerializedName("order_index")
    public String orderIndex;//订单号

    @SerializedName("create_order_time")
    public String createOrderTime;//下单时间

    @SerializedName("order_id")//订单号
    public String orderId;

    @SerializedName("order_name")//发货方名字
    public String order_name;

    @SerializedName("order_total_lebi")//乐享币总价
    public String order_total_lebi;

    @SerializedName("order_total_lefen")//乐分币总价
    public String order_total_lefen;

    @SerializedName("order_total_coupon")//抵扣券总价
    public String order_total_coupon;

    @SerializedName("order_total_voucher")//零购券总价
    public String order_total_voucher;

    @SerializedName("user_lebi")//用户奖励
    public String userLeBi;

    @SerializedName("user_integral")//用户乐分币奖励
    public String userIntegral;

    @SerializedName("user_coupon")//用户抵扣券奖励
    public String userCoupon;

//    @SerializedName("")
//    public String

}

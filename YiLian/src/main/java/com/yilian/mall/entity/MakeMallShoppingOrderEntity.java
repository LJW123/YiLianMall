package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/9.
 */
public class MakeMallShoppingOrderEntity extends BaseEntity implements Serializable {


    @SerializedName("create_order_time")
    public String createOrderTime;//下单时间
    @SerializedName("order_total_lebi") //乐享币总价
    public String order_total_lebi;
    @SerializedName("order_total_lefen")//乐分币总价
    public String order_total_lefen;
    @SerializedName("order_total_coupon")//抵扣券总价
    public String order_total_coupon;

    @SerializedName("user_lebi")//用户奖励
    public String userLebi;

    @SerializedName("user_integeral")//用户乐分币奖励
    public String userIntegeral;

    @SerializedName("user_coupon")//用户抵扣券奖励
    public String userCoupon;

    @SerializedName("order_total_voucher")//零购券总价
    public String orderTotalVoucher;

    @SerializedName("list")
    public ArrayList<MakeMallShopping> list;

    public class MakeMallShopping implements Serializable {
        @SerializedName("order_index")
        public String orderIndex;//
        @SerializedName("order_name")
        public String order_name;
        @SerializedName("order_id")
        public String orderId;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/6/13 0013.
 */
public class MerchantPayEntity extends BaseEntity{

    /**
     * cash :
     * order_id :
     * lev_name :
     * lebi :
     * fail_time :
     * service_time :
     * merchant_type :
     */

    @SerializedName("cash")
    public String cash;//需要支付的金额
    @SerializedName("order_id")
    public String orderId;//订单号
    @SerializedName("lev_name")
    public String levName;//会员等级名称
    @SerializedName("lebi")
    public String lebi;//用户奖励
    @SerializedName("fail_time")
    public String failTime;//年费状态 0未缴纳 否则为到期时间戳
    @SerializedName("service_time")
    public String serviceTime;//服务到期时间
    @SerializedName("merchant_type")
    public String merchantType;//商家类型
    @SerializedName("order_index")
    public String orderIndex;//订单自增ID
    @SerializedName("status")
    public String status;//0入驻，1需交
}

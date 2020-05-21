package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2016/12/8 0008.
 * 外卖退款状态追踪实体类
 */

public class MTRefundStatusEntity extends BaseEntity {

    @SerializedName("name")
    public String name;

    @SerializedName("price")
    public String price;

    @SerializedName("ref_style") //退款方式
    public String refStyle;

    @SerializedName("plan_time") //预计到款时间
    public String planTime;

    @SerializedName("status") //订单状态 退款状态 1待处理 2退款中 3已退款
    public String status;

    @SerializedName("create_time") //申请时间
    public String createTime;

    @SerializedName("datetime") //退款时间
    public String datetime;
}

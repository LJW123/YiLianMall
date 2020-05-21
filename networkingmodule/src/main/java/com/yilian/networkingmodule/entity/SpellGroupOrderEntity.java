package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/5/19 0019.
 */
public class SpellGroupOrderEntity extends BaseEntity {
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("group_id")
    public String groupId;
    @SerializedName("activity_id")
    public String activityId;

}

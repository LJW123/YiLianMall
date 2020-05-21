package com.yilian.mall.entity;/**
 * Created by  on 2017/3/12 0012.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2017/3/12 0012.
 * 删除订单返回实体类
 */
public class DeleteOrderEntity extends BaseEntity {

    /**
     * data : {}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
    }
}

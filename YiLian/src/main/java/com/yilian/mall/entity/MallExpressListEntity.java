package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/1/26.
 */
public class MallExpressListEntity extends BaseEntity {

    public List<ExpressLists> list;
    public class ExpressLists{
        @SerializedName("express_id")
        public String expressId;//邮费模板ID
        @SerializedName("express_name")
        public String expressName;//快递名称
        @SerializedName("express_pic")
        public String expressPic;//快递图标
        @SerializedName("express_price")
        public float expressPrice;//邮费价格
        @SerializedName("is_used")
        public int isUsed;
    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MallProviderEntity extends BaseEntity {
    @SerializedName("list")
    public ArrayList<mallProvider> list;

    public class mallProvider{
        @SerializedName("supplier_id")//旗舰店id
        public String supplier_id;
        @SerializedName("supplier_name")//旗舰店名字
        public String supplier_name;
        @SerializedName("supplier_icon")//旗舰店缩列图
        public String supplier_icon;
        @SerializedName("supplier_desp")//旗舰店描述
        public String supplier_desp;
        @SerializedName("total_sale")//商品累计销量
        public String total_sale;
        @SerializedName("count")//商品数量
        public String count;

    }
}

package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MerchantEntity extends BaseEntity {
    @SerializedName("filiale_id")//兑换中心id,没有兑换中心时为0
    public int filiale_id;

    @SerializedName("info")
    public Info info;

    @SerializedName("list")
    public ArrayList<ListEntity> list;

    public class Info{
        @SerializedName("shop_index")//兑换中心分店唯一ID
        public int shop_index;
        @SerializedName("shop_filiale_id")//兑换中心唯一ID
        public String shop_filiale_id;
        @SerializedName("shop_name")//兑换中心门店标题
        public String shop_name;
        @SerializedName("shop_province")//省
        public String shop_province;
        @SerializedName("shop_city")//市
        public String shop_city;
        @SerializedName("shop_county")//县
        public String shop_county;
        @SerializedName("shop_address")//详细地址
        public String shop_address;
        @SerializedName("shop_longitude")
        public String shop_longitude;
        @SerializedName("shop_latitude")
        public String shop_latitude;
        @SerializedName("shop_tel")//联系电话
        public String shop_tel;
        @SerializedName("shop_desp")//描述
        public String shop_desp;
        @SerializedName("shop_type")//0分店，1总店
        public String shop_type;
        @SerializedName("shop_worktime")//营业时间
        public String shop_worktime;
        @SerializedName("graded")//兑换中心门店评分10-50之间，代表1-5颗星
        public String graded;
        @SerializedName("images")
        public ArrayList<Images> images;
    }
    public class ListEntity {
        @SerializedName("goods_index")//商品id
        public String goods_index;
        @SerializedName("goods_name")//商品姓名
        public String goods_name;
        @SerializedName("goods_icon")//商品缩列图
        public String goods_icon;
        @SerializedName("goods_price")//商品价格
        public String goods_price;
        @SerializedName("goods_type")//商品类型3乐分4乐享100省分
        public int goods_type;
        @SerializedName("goods_popular")//人气浏览量
        public String goods_popular;
        @SerializedName("goods_grade")//评分
        public String goods_grade;
        @SerializedName("goods_sale")//销量
        public String goods_sale;
        @SerializedName("coupon")
        public String coupon;
    }

    public class Images{
        @SerializedName("photo_name")
        public String photo_name;
        @SerializedName("photo_path")
        public String photo_path;
    }
}

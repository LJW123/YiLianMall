package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MallCategoryHotListEntity extends BaseEntity {
    @SerializedName("list")
    public ArrayList<MallCategoryHot> list;
    public Supplier supplier;

    public class MallCategoryHot {
        @SerializedName("goods_id")//商品id
        public String goods_id;
        @SerializedName("goods_name")//商品名字
        public String goods_name;
        @SerializedName("goods_icon")//商品图片
        public String goods_icon;
        @SerializedName("goods_price")//商品价格
        public String goods_price;
        @SerializedName("goods_region")//商品所属地区
        public String goods_region;
        @SerializedName("goods_supplier")//商品供货商id
        public String goods_supplier;
        @SerializedName("goods_type")//商品类型3乐分区4乐享区100省分商城
        public String goods_type;
        @SerializedName("goods_sale")//商品销量
        public String goods_sale;
        @SerializedName("coupon")
        public String coupon;
    }

    public class Supplier {
        @SerializedName("count")
        public String count;
        @SerializedName("supplier_id")
        public String supplierId;
        @SerializedName("supplier_name")
        public String supplierNmae;
        @SerializedName("supplier_desp")
        public String supplierDesp;
        @SerializedName("supplier_icon")
        public String supplierIcon;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/10/10 0010.
 */

public class MerchantManagerListEntity extends HttpResultBean implements Serializable {

    @SerializedName("data")
    public ArrayList<DataBean> data;


    public class DataBean {
        @SerializedName("goods_index")
        public String goods_index;
        @SerializedName("goods_name") //商品名称
        public String goods_name;
        @SerializedName("goods_price") //市场价
        public String goods_price;
        @SerializedName("goods_cost") //用户支付价
        public String goods_cost;
        @SerializedName("retail_price") //供货商结算价
        public String retail_price;
        @SerializedName("goods_integral") //易划算价格
        public String goods_integral;
        @SerializedName("is_integral")
        public String is_integral;
        @SerializedName("is_gou")
        public String is_gou;
        @SerializedName("goods_icon")
        public String goods_icon;
        @SerializedName("goods_brand")
        public String goods_brand;
        @SerializedName("goods_mass")
        public String goods_mass;
        @SerializedName("goods_unit")
        public String goods_unit;
        @SerializedName("goods_status") //1 2 3 审核通过状态 4 审核拒绝
        public String goods_status;
        @SerializedName("goods_sale") //销量
        public String goods_sale;
        @SerializedName("goods_reject_reasons")
        public String goods_reject_reasons;
        @SerializedName("goods_sku_count") //库存
        public String goods_sku_count;
        @SerializedName("goods_supplier") //供货商id
        public String goods_supplier;
        @SerializedName("refuse_reason") //拒绝原因
        public String refuse_reason;
        @SerializedName("submit_status") //0 待提交 1待审核
        public String submit_status;
        @SerializedName("is_normal") //商城icon
        public String is_normal;
        @SerializedName("is_yhs") //yhs icon
        public String is_yhs;
        @SerializedName("is_jfg") //jfg icon
        public String is_jfg;
        @SerializedName("authority") //1奖券购显示  0益划算显示 2俩个都显示
        public String authority;
    }



}

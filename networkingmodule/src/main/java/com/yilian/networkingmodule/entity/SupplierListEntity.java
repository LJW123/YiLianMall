package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by LYQ on 2017/10/10.
 */

public class SupplierListEntity extends HttpResultBean {

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        /**
         * order_id : 46
         * service_order : 2017091818322645809
         * old_order : 2017091818142894528
         * order_contacts : 李静
         * order_phone : 18530063088
         * order_address : 河南省郑州市管城回族区建正·东方中心D座123390
         * type : 0  返修退货
         * barter_type : 1
         * status : 0
         * goods_price : 10000
         * goods_cost : 9000
         * goods_count : 1
         * goods_id : 1980
         * service_time : 1505730747
         * goods_name : 商品2
         * goods_icon : supplier/images/dd979bacae64a1f8e61127cfeba85a91befbea64_835803058938257386.jpg
         * goods_norms : 尺寸 S
         * goods_filiale : 0
         * mall_goods_index : 2160
         * goods_integral : 0
         * pay_style : 0
         * goods_retail : 8800
         * return_integral : 1000
         * goods_type : 0
         * goods_discount : 0
         * user_express_number :
         * user_express_company :
         * merchant_express_company :
         * merchant_express_number :
         */

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("service_order")
        public String serviceOrder;//短的orderId
        @SerializedName("old_order")
        public String oldOrder;//原长订单号
        @SerializedName("order_contacts")
        public String orderContacts;
        @SerializedName("order_phone")
        public String orderPhone;
        @SerializedName("order_address")
        public String orderAddress;
        @SerializedName("type")
        public String type;//退货 换货
        @SerializedName("barter_type")
        public String barterType;
        @SerializedName("status")
        public String status;//当前的状态 （返货 换修0已取消，1待审核2审核拒绝3待退货4维修中/换货中5待发货6待收货7已完成）(退货 0已取消，1待审核2审核拒绝3待退货4待收货，5待退款，6已完成)
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_count")
        public String goodsCount;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("service_time")
        public String serviceTime;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_norms")
        public String goodsNorms;
        @SerializedName("goods_filiale")
        public int goodsFiliale;
        @SerializedName("mall_goods_index")
        public String mallGoodsIndex;
        @SerializedName("goods_integral")
        public String goodsIntegral;
        @SerializedName("pay_style")
        public String payStyle;
        @SerializedName("goods_retail")
        public String goodsRetail;
        @SerializedName("return_integral")
        public int returnIntegral;
        @SerializedName("goods_type")
        public String goodsType;
        @SerializedName("goods_discount")
        public int goodsDiscount;
        @SerializedName("user_express_number")
        public String userExpressNumber;//买家退货单号
        @SerializedName("user_express_company")
        public String userExpressCompany;//买家退货公司简称
        @SerializedName("merchant_express_company")
        public String merchantExpressCompany;//卖家退货公司简称
        @SerializedName("merchant_express_number")
        public String merchantExpressNumber;//卖家退货单号

        /**
         * 获得益豆
         */
        @SerializedName("return_bean")
        public String returnBean;
        /**
         * 平台附赠益豆
         */
        @SerializedName("subsidy")
        public String subsidy;

    }
}

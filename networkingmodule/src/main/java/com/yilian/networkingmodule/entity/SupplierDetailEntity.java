package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by LYQ on 2017/10/10.
 */

public class SupplierDetailEntity  extends HttpResultBean{

    /**
     * list : {"order_id":"46","service_order":"2017091818322645809","old_order":"2017091818142894528","order_contacts":"李静","order_phone":"18530063088","order_address":"河南省郑州市管城回族区建正·东方中心D座123390","type":0,"barter_type":"1","status":"0","goods_price":"10000","goods_cost":"9000","goods_coupon":0,"total_price":0,"goods_count":"1","goods_sku":"690:709","goods_id":"1980","service_time":"1505730747","goods_name":"商品2","goods_icon":"supplier/images/dd979bacae64a1f8e61127cfeba85a91befbea64_835803058938257386.jpg","goods_norms":"","goods_filiale":0,"mall_goods_index":"2160","goods_integral":"0","pay_style":"0","goods_retail":"8800","remark":"撒丁岛","images":"app/images/mall/20170918/6566844903401203_5885065_image.jpg","name":"3088","phone":"18530063088","check_time":"0","refuse":"","user_express_time":"0","user_express_number":"0","user_express_company":"","merchant_express_time":"0","merchant_express_number":"","merchant_express_company":"","user_confirm_time":"1505731211","merchant_confirm_time":"1505706001","merchant_payment_time":"1505705507"}
     */

    @SerializedName("list")
    public ListBean list;

    public static class ListBean {
        /**
         * order_id : 46
         * service_order : 2017091818322645809
         * old_order : 2017091818142894528
         * order_contacts : 李静
         * order_phone : 18530063088
         * order_address : 河南省郑州市管城回族区建正·东方中心D座123390
         * type : 0
         * barter_type : 1
         * status : 0
         * goods_price : 10000
         * goods_cost : 9000
         * goods_coupon : 0
         * total_price : 0
         * goods_count : 1
         * goods_sku : 690:709
         * goods_id : 1980
         * service_time : 1505730747
         * goods_name : 商品2
         * goods_icon : supplier/images/dd979bacae64a1f8e61127cfeba85a91befbea64_835803058938257386.jpg
         * goods_norms :
         * goods_filiale : 0
         * mall_goods_index : 2160
         * goods_integral : 0
         * pay_style : 0
         * goods_retail : 8800
         * remark : 撒丁岛
         * images : app/images/mall/20170918/6566844903401203_5885065_image.jpg
         * name : 3088
         * phone : 18530063088
         * check_time : 0
         * refuse :
         * user_express_time : 0
         * user_express_number : 0
         * user_express_company :
         * merchant_express_time : 0
         * merchant_express_number :
         * merchant_express_company :
         * user_confirm_time : 1505731211
         * merchant_confirm_time : 1505706001
         * merchant_payment_time : 1505705507
         */

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("service_order")
        public String serviceOrder;
        @SerializedName("old_order")
        public String oldOrder;//原长订单号
        @SerializedName("old_order_index")
        public String oldOrderIndex;//原短订单号
        @SerializedName("order_contacts")
        public String orderContacts;
        @SerializedName("order_phone")
        public String orderPhone;
        @SerializedName("order_address")
        public String orderAddress;
        @SerializedName("type")
        public String type;
        @SerializedName("barter_type")
        public String barterType;
        @SerializedName("status")
        public String status;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_coupon")
        public int goodsCoupon;
        @SerializedName("total_price")
        public int totalPrice;
        @SerializedName("goods_count")
        public String goodsCount;
        @SerializedName("goods_sku")
        public String goodsSku;
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
        public String returnIntegral;
        @SerializedName("remark")
        public String remark;
        @SerializedName("images")
        public String images;//以逗号分割
        @SerializedName("name")
        public String name;
        @SerializedName("phone")
        public String phone;
        @SerializedName("check_time")
        public String checkTime;
        @SerializedName("refuse")
        public String refuse;
        @SerializedName("user_express_time")
        public String userExpressTime;//买家退货时间
        @SerializedName("user_express_number")
        public String userExpressNumber;//买家退货单号
        @SerializedName("user_express_company")
        public String userExpressCompany;//买家退货公司简称
        @SerializedName("merchant_express_time")
        public String merchantExpressTime;//(换修货返回此字段) 卖家发货时间
        @SerializedName("merchant_express_number")
        public String merchantExpressNumber;//（换修货返回此字段）卖家发货单号
        @SerializedName("merchant_express_company")
        public String merchantExpressCompany;//（换修货返回此字段）卖家返货公司简称
        @SerializedName("user_confirm_time")
        public String userConfirmTime;//（换修货返回此字段） 买家确认收货时间
        @SerializedName("merchant_confirm_time")
        public String merchantConfirmTime;//（退货返回此字段）卖家确认收货时间
        @SerializedName("merchant_payment_time")
        public String merchantPaymentTime;//（退货返回此字段）卖家确认退款时间

        /**
         * 获得
         */
        @SerializedName("return_bean")
        public String returnBean;
        /**
         * 平台附赠
         */
        @SerializedName("subsidy")
        public String subsidy;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by LYQ on 2017/9/29.
 */

public class MerchantOrderEntity extends HttpResultBean {

    /**
     * tel : 400-152-5159
     * order_index : 2274
     * order_id : 2017092910145976870
     * order_status : 3
     * order_remark :
     * order_time : 1506651299
     * payment_deal_id : 0
     * payment_time : 1506651305
     * order_goods_price : 4000
     * order_freight_price : 900
     * order_total_price : 4900
     * order_integral_price : 0
     * order_phone : 15238664345
     * order_contacts : 暨南
     * order_address : 河南省郑州市管城回族区世正商·建正东方中心顺流逆流弄
     * confirm_time : 0
     * goods_num :
     * order_type : 0
     * unuse_time : 0
     * flag : 1
     * server_time : 1506654662
     * delivery_time : 1506654662
     * return_integral : 2500
     * goods : [{"goods_name":"abc商品01","goods_unit":"件","goods_discount":"","goods_type":"0","goods_icon":"supplier/images/8eeaff822d83f2076978a39da3b3b17e201425c9_139-161104154957.jpg","goods_norms":"","order_goods_index":"2445","goods_id":"1978","goods_count":"1","goods_sku":"0:0","goods_status":"0","parcel_index":"0","goods_after_sale":0,"type":"0","goods_price":"5000","goods_cost":"4000","goods_integral":0,"goods_retail":"3500","return_integral":2500,"filiale_id":0}]
     * supplier_id : 98
     * parcels : [{"parcel_index":0,"parcel_status":0,"express_time":0,"express_company":0,"express_num":0,"express_info":0,"confirm_time":0}]
     * order_name : ABC旗舰店1
     */
    @SerializedName("order_quan_price")
    public String daiGouQuanMoney;
    @SerializedName("profit")
    public int profitStatus; //针对已完成订单  0未发奖励  1已送奖券
    @SerializedName("tel")
    public String tel;
    @SerializedName("order_index")
    public String orderIndex;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("order_status")
    public String orderStatus; //订单的状态 0已取消，1待支付 2待出库（待备货）3正在出库（点击完成备货按钮/待发货） 4已部分发货5已全部发货&待收货6已完成
    @SerializedName("order_remark")
    public String orderRemark;
    @SerializedName("order_time")
    public String orderTime;
    @SerializedName("payment_deal_id")
    public String paymentDealId;
    @SerializedName("payment_time")
    public String paymentTime;
    @SerializedName("order_goods_price")
    public String orderGoodsPrice;
    @SerializedName("order_freight_price")
    public String orderFreightPrice;
    @SerializedName("order_total_price")
    public String orderTotalPrice;
    @SerializedName("order_integral_price")
    public String orderIntegralPrice;
    @SerializedName("order_phone")
    public String orderPhone;
    /**
     * 联系人
     */
    @SerializedName("order_contacts")
    public String orderContacts;
    @SerializedName("order_address")
    public String orderAddress;
    @SerializedName("confirm_time")
    public String confirmTime;
    @SerializedName("goods_num")
    public String goodsNum;
    @SerializedName("order_type")
    public String orderType;
    @SerializedName("unuse_time")
    public int unuseTime;
    @SerializedName("flag")
    public int flag;
    @SerializedName("server_time")
    public int serverTime;
    @SerializedName("delivery_time")
    public String deliveryTime;
    @SerializedName("return_integral")
    public int returnIntegral;
    @SerializedName("supplier_id")
    public String supplierId;
    @SerializedName("order_name")
    public String orderName;
    @SerializedName("goods")
    public List<GoodsBean> goods;
    @SerializedName("parcels")
    public List<ParcelsBean> parcels;
    @SerializedName("profit_time")
    public String profitTime;
    /**
     * 获得总益豆
     */
    @SerializedName("all_given_bean")
    public float allGivenBean;
    /**
     * 平台附赠总益豆
     */
    @SerializedName("all_subsidy")
    public float allSubsidy;


    public class GoodsBean extends MerchantManageOrderBaseEntity {
        public boolean isChecked;
        /**
         * goods_name : abc商品01
         * goods_unit : 件
         * goods_discount :
         * goods_type : 0
         * goods_icon : supplier/images/8eeaff822d83f2076978a39da3b3b17e201425c9_139-161104154957.jpg
         * goods_norms :
         * order_goods_index : 2445
         * goods_id : 1978
         * goods_count : 1
         * goods_sku : 0:0
         * goods_status : 0
         * parcel_index : 0
         * goods_after_sale : 0
         * type : 0
         * goods_price : 5000
         * goods_cost : 4000
         * goods_integral : 0
         * goods_retail : 3500
         * return_integral : 2500
         * filiale_id : 0
         */

        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_unit")
        public String goodsUnit;
        @SerializedName("goods_discount")
        public String goodsDiscount;
        /**
         * 商品类别 0 普通商品 1yhs 2jfg
         */
        @SerializedName("goods_type")
        public String goodsType;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_norms")
        public String goodsNorms;
        @SerializedName("order_goods_index")
        public String orderGoodsIndex;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_count")
        public String goodsCount;
        @SerializedName("goods_sku")
        public String goodsSku;
        @SerializedName("goods_status")
        public String goodsStatus;//商品的状态  0未发货、1已发货，2已完成待评价，3已评价
        @SerializedName("parcel_index")
        public String parcelIndex;
        @SerializedName("goods_after_sale")
        public String goodsAfterSale;
        @SerializedName("type")
        public String type;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_integral")
        public String goodsIntegral;
        @SerializedName("goods_retail")
        public String goodsRetail;
        @SerializedName("return_integral")
        public String returnIntegral;
        @SerializedName("filiale_id")
        public String filialeId;
        /**
         * 获得益豆
         */
        @SerializedName("given_bean")
        public float givenBean;
        /**
         * 平台附赠益豆
         */
        @SerializedName("subsidy")
        public float subsidy;


        @Override
        public int getItemType() {
            return MerchantManageOrderBaseEntity.GOOD;
        }
    }

    public class ParcelsBean {
        /**
         * parcel_index : 0
         * parcel_status : 0
         * express_time : 0
         * express_company : 0
         * express_num : 0
         * express_info : 0
         * confirm_time : 0
         */

        @SerializedName("parcel_index")
        public String parcelIndex;
        @SerializedName("parcel_status")
        public String parcelStatus;
        @SerializedName("express_time")
        public String expressTime;
        @SerializedName("express_company")
        public String expressCompany;
        @SerializedName("express_num")
        public String expressNum;
        @SerializedName("express_info")
        public String expressInfo;
        @SerializedName("confirm_time")
        public String confirmTime;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by liuyuqi on 2017/8/29 0029.
 */

public class ScOrderListEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * goods_info : [{"goods_after_sale":"0","goods_cost":"900","goods_count":"1","goods_index":"2052","goods_name":"费列罗，你值得拥有","goods_norms":"","goods_price":"1800","goods_sku":"0:0","merchant_id":"198","merchant_integral":800,"order_goods_index":"60","order_index":"45","retail_price":"500","supplier_id":"143","user_id":"6713908494569117","user_integral":2000},{"goods_after_sale":"0","goods_cost":"3600","goods_count":"2","goods_index":"2059","goods_name":"水果沙拉","goods_norms":"","goods_price":"4500","goods_sku":"0:0","merchant_id":"198","merchant_integral":3200,"order_goods_index":"61","order_index":"45","retail_price":"2000","supplier_id":"143","user_id":"6713908494569117","user_integral":8000}]
         * merchant_integral : 7200
         * merchant_type : 1
         * order_consumer : 6713908494569117
         * order_goods_price : 8100
         * order_id : 2017083110143391865
         * order_index : 45
         * order_status : 1
         * order_supplier_price : 4500
         * order_total_count : 3
         * phone : 18530809275
         * total_count : 3
         * user_integral : 18000
         */

        @SerializedName("merchant_integral")
        public long merchantIntegral;
        @SerializedName("merchant_type")
        public String merchantType;
        @SerializedName("order_consumer")
        public String orderConsumer;
        @SerializedName("order_goods_price")
        public String orderGoodsPrice;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("order_status")
        public String orderStatus;
        @SerializedName("order_supplier_price")
        public String orderSupplierPrice;
        @SerializedName("order_total_count")
        public String orderTotalCount;
        @SerializedName("phone")
        public String phone;
        @SerializedName("total_count")
        public String totalCount;
        @SerializedName("user_integral")
        public long userIntegral;
        @SerializedName("goods_info")
        public List<GoodsInfoBean> goodsInfo;

        public static class GoodsInfoBean extends BaseBarcodeEntity{
            /**
             * goods_after_sale : 0
             * goods_cost : 900
             * goods_count : 1
             * goods_index : 2052
             * goods_name : 费列罗，你值得拥有
             * goods_norms :
             * goods_price : 1800
             * goods_sku : 0:0
             * merchant_id : 198
             * merchant_integral : 800
             * order_goods_index : 60
             * order_index : 45
             * retail_price : 500
             * supplier_id : 143
             * user_id : 6713908494569117
             * user_integral : 2000
             */

            @SerializedName("goods_after_sale")
            public String goodsAfterSale;
            @SerializedName("goods_cost")
            public String goodsCost;
            @SerializedName("goods_count")
            public String goodsCount;
            @SerializedName("goods_index")
            public String goodsIndex;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("goods_norms")
            public String goodsNorms;
            @SerializedName("goods_price")
            public String goodsPrice;
            @SerializedName("goods_sku")
            public String goodsSku;
            @SerializedName("merchant_id")
            public String merchantId;
            @SerializedName("merchant_integral")
            public int merchantIntegral;
            @SerializedName("order_goods_index")
            public String orderGoodsIndex;
            @SerializedName("order_index")
            public String orderIndex;
            @SerializedName("retail_price")
            public String retailPrice;
            @SerializedName("supplier_id")
            public String supplierId;
            @SerializedName("user_id")
            public String userId;
            @SerializedName("user_integral")
            public int userIntegral;

            @Override
            public int getItemType() {
                    return BaseBarcodeEntity.ITEMVIEW;
            }
        }
    }


}

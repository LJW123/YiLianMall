package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuyuqi on 2017/5/23 0023.
 */
public class SpellGroupOrderInfoEntity extends BaseEntity {

    /**
     * data : {"activity_id":"3","amount":"2","city_name":"东方","consumer_id":"5702006601841715","done_group_time":1495697742,"goods_count":"1","goods_icon":"goods/147832599919206.jpg","goods_name":"欧根纱短裙_古谷浪半身裙2016新款女士欧根纱包臀裙高腰裙","goods_sku_name":"","group_id":"0","label":"抽奖团","order_address":"世正商·建正东方中心709","order_city":"127","order_contacts":"小七","order_county":"0","order_id":"2017052509563112749","order_index":"9","order_phone":"18135697270","order_province":"11","order_remark":"","order_time":"1495677391","pay_time":"0","pay_type":"0","pay_type_name":"未支付","phone":"18135697270","prize_status":"1","province_name":"河南","sku_index":"7132","tel":"400-001-2411"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * activity_id : 3
         * amount : 2
         * city_name : 东方
         * consumer_id : 5702006601841715
         * done_group_time : 1495697742
         * goods_count : 1
         * goods_icon : goods/147832599919206.jpg
         * goods_name : 欧根纱短裙_古谷浪半身裙2016新款女士欧根纱包臀裙高腰裙
         * goods_sku_name :
         * group_id : 0
         * label : 抽奖团
         * order_address : 世正商·建正东方中心709
         * order_city : 127
         * order_contacts : 小七
         * order_county : 0
         * order_id : 2017052509563112749
         * order_index : 9
         * order_phone : 18135697270
         * order_province : 11
         * order_remark :
         * order_time : 1495677391
         * pay_time : 0
         * pay_type : 0
         * pay_type_name : 未支付
         * phone : 18135697270
         * prize_status : 1
         * province_name : 河南
         * sku_index : 7132
         * tel : 400-001-2411
         */

        @SerializedName("activity_id")
        public String activityId;
        @SerializedName("amount")
        public String amount;
        @SerializedName("city_name")
        public String cityName;
        @SerializedName("consumer_id")
        public String consumerId;
        @SerializedName("done_group_time")
        public int doneGroupTime;
        @SerializedName("goods_count")
        public String goodsCount;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_sku_name")
        public String goodsSkuName;
        @SerializedName("group_id")
        public String groupId;
        @SerializedName("label")
        public String label;
        @SerializedName("order_address")
        public String orderAddress;
        @SerializedName("order_city")
        public String orderCity;
        @SerializedName("order_contacts")
        public String orderContacts;
        @SerializedName("order_county")
        public String orderCounty;
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("order_phone")
        public String orderPhone;
        @SerializedName("order_province")
        public String orderProvince;
        @SerializedName("order_remark")
        public String orderRemark;
        @SerializedName("order_time")
        public String orderTime;
        @SerializedName("pay_time")
        public String payTime;
        @SerializedName("pay_type")
        public String payType;
        @SerializedName("pay_type_name")
        public String payTypeName;
        @SerializedName("phone")
        public String phone;
        @SerializedName("prize_status")
        public String prizeStatus;
        @SerializedName("province_name")
        public String provinceName;
        @SerializedName("sku_index")
        public String skuIndex;
        @SerializedName("tel")
        public String tel;
    }
}

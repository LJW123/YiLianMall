package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuyuqi on 2017/5/16 0016.
 */
public class SpellGroupDetailsEntity extends BaseEntity {

    /**
     * data : {"goods_id":"4862","goods_sku":"2:583,3:584","amount":"12","title":"12","intro":"213","prize_number":"1","start_time":"1494931014","end_time":"1494931015","group_condition":"1","time_limit":"12","people_limit":"12","is_red_envelope":"0","is_direct_buy":"0","total_number":"0","done_number":"0","fail_number":"0","progress_number":"0","status":"0","desc_tags":["精选品牌","品质保障","官方售后","48小时发货"],"goods_sku_count":1,"GoodsSkuPriceBean":[{"sku_index":"9617","sku_goods":"4862","sku_info":"2:583,3:584","sku_market_price":"10000","sku_supply_price":"9000","sku_lefen_price":"8000","sku_integral_price":"0","sku_integral_money":"0","sku_repertory":"1008","sku_inventory":"992","sku_defective":"0","sku_update_time":"1492737571","sku_code":"123123123123","is_del":"0","sku_cost_price":"9000","sku_retail_price":"8000","change_time":null}],"goods_icon":"goods/147832446175605.jpg","goods_name":"adidasi-韩国进口棉袜_韩国进口女士棉袜beimon品牌短袜时尚全棉防臭休闲","goods_price":"10000","goods_filiale":0,"goods_supplier":"318","index":"9","goods_album":["goods/147832446175605.jpg","goods/147832446210907.jpg","goods/147832446323105.jpg","goods/147832446394955.jpg","goods/147832446425918.jpg","goods/147832446565809.jpg","goods/147832446638191.jpg"],"goods_sku_property":[{"sku_index":"2","sku_name":"尺码","supplier_belong":"-1"},{"sku_index":"3","sku_name":"颜色","supplier_belong":"-1"}],"goods_sku_values":[{"sku_index":"583","sku_name":"有码","sku_parent":"2","supplier_belong":"-1"},{"sku_index":"584","sku_name":"无色","sku_parent":"3","supplier_belong":"318"}]}
     */

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        /**
         * goods_id : 4862
         * goods_sku : 2:583,3:584
         * amount : 12
         * title : 12
         * intro : 213
         * prize_number : 1
         * start_time : 1494931014
         * end_time : 1494931015
         * group_condition : 1
         * time_limit : 12
         * people_limit : 12
         * is_red_envelope : 0
         * is_direct_buy : 0
         * total_number : 0
         * done_number : 0
         * fail_number : 0
         * progress_number : 0
         * status : 0
         * desc_tags : ["精选品牌","品质保障","官方售后","48小时发货"]
         * goods_sku_count : 1
         * GoodsSkuPriceBean : [{"sku_index":"9617","sku_goods":"4862","sku_info":"2:583,3:584","sku_market_price":"10000","sku_supply_price":"9000","sku_lefen_price":"8000","sku_integral_price":"0","sku_integral_money":"0","sku_repertory":"1008","sku_inventory":"992","sku_defective":"0","sku_update_time":"1492737571","sku_code":"123123123123","is_del":"0","sku_cost_price":"9000","sku_retail_price":"8000","change_time":null}]
         * goods_icon : goods/147832446175605.jpg
         * goods_name : adidasi-韩国进口棉袜_韩国进口女士棉袜beimon品牌短袜时尚全棉防臭休闲
         * goods_price : 10000
         * goods_filiale : 0
         * goods_supplier : 318
         * index : 9
         * goods_album : ["goods/147832446175605.jpg","goods/147832446210907.jpg","goods/147832446323105.jpg","goods/147832446394955.jpg","goods/147832446425918.jpg","goods/147832446565809.jpg","goods/147832446638191.jpg"]
         * goods_sku_property : [{"sku_index":"2","sku_name":"尺码","supplier_belong":"-1"},{"sku_index":"3","sku_name":"颜色","supplier_belong":"-1"}]
         * goods_sku_values : [{"sku_index":"583","sku_name":"有码","sku_parent":"2","supplier_belong":"-1"},{"sku_index":"584","sku_name":"无色","sku_parent":"3","supplier_belong":"318"}]
         */

        @SerializedName("amount")
        public String amount;
        @SerializedName("done_number")
        public String doneNumber;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("fail_number")
        public String failNumber;
        @SerializedName("goods_filiale")
        public int goodsFiliale;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_index")
        public String goodsIndex;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_sku")
        public String goodsSku;
        @SerializedName("goods_sku_count")
        public int goodsSkuCount;
        @SerializedName("goods_supplier")
        public String goodsSupplier;
        @SerializedName("group_condition")
        public String groupCondition;
        @SerializedName("index")
        public String index;
        @SerializedName("intro")
        public String intro;
        @SerializedName("is_direct_buy")
        public String isDirectBuy;
        @SerializedName("is_red_envelope")
        public String isRedEnvelope;
        @SerializedName("order_fregiht")
        public String orderFregiht;
        @SerializedName("people_limit")
        public String peopleLimit;
        @SerializedName("prize_number")
        public String prizeNumber;
        @SerializedName("progress_number")
        public String progressNumber;
        @SerializedName("start_time")
        public String startTime;
        @SerializedName("status")
        public String status;
        @SerializedName("time")
        public int time;
        @SerializedName("time_limit")
        public String timeLimit;
        @SerializedName("title")
        public String title;
        @SerializedName("total_number")
        public String totalNumber;
        @SerializedName("desc_tags")
        public List<String> descTags;
        @SerializedName("goods_album")
        public List<String> goodsAlbum;
        @SerializedName("GoodsSkuPriceBean")
        public List<GoodsSkuPriceBean> goodsSkuPrice;
        @SerializedName("goods_sku_property")
        public List<GoodsSkuPropertyBean> goodsSkuProperty;
        @SerializedName("goods_sku_values")
        public List<GoodsSkuPropertyBean> goodsSkuValues;
        @SerializedName("yesOrNo")
        public String isJoinOpenGroup;



        public class GoodsSkuPriceBean {
            /**
             * sku_index : 9617
             * sku_goods : 4862
             * sku_info : 2:583,3:584
             * sku_market_price : 10000
             * sku_supply_price : 9000
             * sku_lefen_price : 8000
             * sku_integral_price : 0
             * sku_integral_money : 0
             * sku_repertory : 1008
             * sku_inventory : 992
             * sku_defective : 0
             * sku_update_time : 1492737571
             * sku_code : 123123123123
             * is_del : 0
             * sku_cost_price : 9000
             * sku_retail_price : 8000
             * change_time : null
             */

            @SerializedName("sku_index")
            public String skuIndex;
            @SerializedName("sku_goods")
            public String skuGoods;
            @SerializedName("sku_info")
            public String skuInfo;
            @SerializedName("sku_market_price")
            public String skuMarketPrice;
            @SerializedName("sku_supply_price")
            public String skuSupplyPrice;
            @SerializedName("sku_lefen_price")
            public String skuLefenPrice;
            @SerializedName("sku_integral_price")
            public String skuIntegralPrice;
            @SerializedName("sku_integral_money")
            public String skuIntegralMoney;
            @SerializedName("sku_repertory")
            public String skuRepertory;
            @SerializedName("sku_inventory")
            public String skuInventory;
            @SerializedName("sku_defective")
            public String skuDefective;
            @SerializedName("sku_update_time")
            public String skuUpdateTime;
            @SerializedName("sku_code")
            public String skuCode;
            @SerializedName("is_del")
            public String isDel;
            @SerializedName("sku_cost_price")
            public String skuCostPrice;
            @SerializedName("sku_retail_price")
            public String skuRetailPrice;
            @SerializedName("change_time")
            public Object changeTime;
        }

        public class GoodsSkuPropertyBean {
            /**
             * sku_index : 2
             * sku_name : 尺码
             * supplier_belong : -1
             */

            @SerializedName("sku_index")
            public String skuIndex;
            @SerializedName("sku_name")
            public String skuName;
            @SerializedName("supplier_belong")
            public String supplierBelong;
            @SerializedName("sku_parent")
            public String skuParentId;

        }

    }
}

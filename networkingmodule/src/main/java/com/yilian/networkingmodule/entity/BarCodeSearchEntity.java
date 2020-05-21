package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by liuyuqi on 2017/8/29 0029.
 */

public class BarCodeSearchEntity extends HttpResultBean {

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * goods_name : 测试商品库存(勿买)
         * goods_cost : 15000
         * goods_code : 123456789
         * goods_index : 1990
         * .                sku_info : 690:691,695:704
         * goods_norms : 尺寸：L，号码：粉色
         * sku_inventory : 12
         */

        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_cost")
        public String goodsCost;
        @SerializedName("goods_code")
        public String goodsCode;
        @SerializedName("goods_index")
        public String goodsIndex;
        @SerializedName("sku_info")
        public String skuInfo;
        @SerializedName("goods_norms")
        public String goodsNorms;
        @SerializedName("sku_inventory")
        public String skuInventory;
        public int goodsCount =1;//默认的现在库存

        public boolean isCheck;//标记是否选中
    }
}

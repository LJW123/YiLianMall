package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/20 0020.
 */

public class ActMyRecordEntity extends HttpResultBean {

    @SerializedName("list")
    public ArrayList<ListBean> list;

    public class ListBean {
        @SerializedName("order_id")
        public String orderId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("goods_icon")
        public String goodsIcon;
        @SerializedName("join_count")
        public String joinCount;
        @SerializedName("order_status")
        public String orderStatus;
        @SerializedName("order_time")
        public String orderTime;
        @SerializedName("order_type")
        public String orderType;
        @SerializedName("order_index")
        public String orderIndex;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("order_goods_index")
        public String orderGoodsIndex;
        @SerializedName("order_integral")
        public double orderIntegral;
        @SerializedName("act_integral_goods_id")
        public String actIntegralGoodsId;
    }

}

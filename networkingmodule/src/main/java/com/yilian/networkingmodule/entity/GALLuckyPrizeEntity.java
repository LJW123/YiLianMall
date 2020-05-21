package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/11/13 0013.
 */

public class GALLuckyPrizeEntity extends HttpResultBean {

    @SerializedName("staus") //是否中奖 0否 1是
    public String staus;
    @SerializedName("img")
    public String img;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("prize_num")
    public String prizeNum;
    @SerializedName("goods_id")
    public String goodsId;
    @SerializedName("join_count")
    public String joinCount;
}

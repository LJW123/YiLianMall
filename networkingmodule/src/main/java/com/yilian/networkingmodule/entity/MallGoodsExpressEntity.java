package com.yilian.networkingmodule.entity;
/**
 * Created by ASUS on 2016/9/14 0014.
 */

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by  on 2016/9/14 0014
 * 商品详情下订单获取到的运费实体
 */
public class MallGoodsExpressEntity extends HttpResultBean {

    /**
     * express_price : 10
     * express_id :
     */

    @SerializedName("list")
    public List<ListBean> list;

    public static class ListBean {
        @SerializedName("express_price")
        public double expressPrice;
        @SerializedName("express_id")
        public String expressId;
    }
}

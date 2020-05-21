package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/6/29.
 *
 */

public class JDGoodsStoreEntities extends HttpResultBean {
    /**
     * 无货
     */
    public static final int OUT_STOCK=0;
    /**
     * 有货
     */
    public static final int IN_STOCK=1;

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * cart_index : 1898
         * stock : 0
         */

        @SerializedName("cart_index")
        public String cartIndex;
        /**
         * 0无货 1有货
         */
        @SerializedName("stock")
        public int stock;
    }
}

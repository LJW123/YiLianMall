package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author xiaofo on 2018/7/30.
 */

public class SnCheckGoodsStock extends HttpResultBean {
public static final int HAS_STOCK=0;
    @SerializedName("result")
    public List<ResultBean> result;

    public static class ResultBean {
        /**
         * skuId :
         * state :
         * img :
         * goods_count :
         * name :
         */

        @SerializedName("skuId")
        public String skuId;
        //0:现货 1在途 2：无货
        @SerializedName("state")
        public int state;

    }
}

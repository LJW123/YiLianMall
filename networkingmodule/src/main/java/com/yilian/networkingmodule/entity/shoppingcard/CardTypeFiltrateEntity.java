package com.yilian.networkingmodule.entity.shoppingcard;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/11/16 17:10
 * 购物卡类型筛选
 */

public class CardTypeFiltrateEntity extends HttpResultBean{

    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * type : 0
         * typeValue : 全部
         */
        @SerializedName("type")
        public String type;
        @SerializedName("typeValue")
        public String typeValue;
        public boolean isCheck;
    }
}

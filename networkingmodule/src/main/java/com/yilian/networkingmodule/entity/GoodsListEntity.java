package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/7/24 0024.
 */

public class GoodsListEntity extends HttpResultBean {


    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("goods")
        public ArrayList<JPGoodsEntity> goods;
    }
}

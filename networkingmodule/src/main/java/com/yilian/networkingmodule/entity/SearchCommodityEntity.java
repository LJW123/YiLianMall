package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * @author Created by  on 2018/2/12.
 */

public class SearchCommodityEntity extends HttpResultBean {

    @SerializedName("list")
    public List<DataBean> list;

}

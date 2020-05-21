package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 苏宁品牌商品列表
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class SnBrandGoodsListEntity extends HttpResultBean {
    @SerializedName("data")
    private List<SnGoodsAbstractInfoEntity> dataList;

    public List<SnGoodsAbstractInfoEntity> getDataList() {
        if (dataList == null) {
            return new ArrayList<>();
        }
        return dataList;
    }

    public void setDataList(List<SnGoodsAbstractInfoEntity> dataList) {
        this.dataList = dataList;
    }
}

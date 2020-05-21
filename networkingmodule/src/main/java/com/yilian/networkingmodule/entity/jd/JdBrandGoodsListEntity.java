package com.yilian.networkingmodule.entity.jd;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东品牌商品列表
 *
 * @author Created by zhaiyaohua on 2018/5/24.
 */

public class JdBrandGoodsListEntity extends HttpResultBean {
    @SerializedName("data")
    private List<JDGoodsAbstractInfoEntity> dataList;

    public List<JDGoodsAbstractInfoEntity> getDataList() {
        if (dataList == null) {
            return new ArrayList<>();
        }
        return dataList;
    }

    public void setDataList(List<JDGoodsAbstractInfoEntity> dataList) {
        this.dataList = dataList;
    }
}

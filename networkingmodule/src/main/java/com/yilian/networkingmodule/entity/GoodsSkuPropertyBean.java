package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ray_L_Pain on 2017/10/11 0011.
 */

public class GoodsSkuPropertyBean implements Serializable {
    @SerializedName("sku_index")
    public String skuIndex;
    @SerializedName("sku_name")
    public String skuName;
    @SerializedName("supplier_belong")
    public String supplierBelong;
    @SerializedName("sku_parent")
    public String skuParentId;

    @Override
    public String toString() {
        return "GoodsSkuPropertyBean{" +
                "skuIndex='" + skuIndex + '\'' +
                ", skuName='" + skuName + '\'' +
                ", supplierBelong='" + supplierBelong + '\'' +
                ", skuParentId='" + skuParentId + '\'' +
                '}';
    }
}

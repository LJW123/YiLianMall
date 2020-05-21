package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by  on 2016/12/6 0006.
 */

public class JPLocalMTMerchantPackageEntity {

    /**
     * package_id :
     * name :
     * merchant_id :
     * albums :
     * amount :
     */

    @SerializedName("package_id")
    public String packageId;
    @SerializedName("name")
    public String name;
    @SerializedName("merchant_id")
    public String merchantId;
    @SerializedName("package_icon")
    public String packageIcon;
    @SerializedName("price")
    public String price;
}

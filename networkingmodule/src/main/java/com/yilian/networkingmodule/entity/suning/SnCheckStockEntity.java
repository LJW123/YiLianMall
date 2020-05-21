package com.yilian.networkingmodule.entity.suning;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author xiaofo on 2018/7/25.
 */

public class SnCheckStockEntity extends HttpResultBean {

public static final String HAS_STOCK="00";
public static final String AREA_NO_STOCK="01";
public static final String NO_STOCK="02";
public static final String STOCK_NOT_ENOUGH="03";
    /**
     * result : //00：有货01：暂不销售 02：无货 03：库存不足
     */

    @SerializedName("result")
    public String result;
}

package com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel;

import java.io.Serializable;

/**
 * 携程  关键字
 * Created by @author Zg on 2018/8/29.
 */

public class CtripKeywordModel implements Serializable {

    /**
     * 商业区id
     */
    public String zoneId;
    /**
     * 品牌id
     */
    public String brandId;

    /**
     * 显示名字
     */
    public String showName;

    public CtripKeywordModel(String zoneId,String brandId, String showName) {
        this.zoneId = zoneId;
        this.brandId = brandId;
        this.showName = showName;
    }

    public CtripKeywordModel() {
    }
}

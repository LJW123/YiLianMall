package com.yilian.networkingmodule.entity.ctrip.ctripEventBusModel;

import java.io.Serializable;
import java.util.List;

/**
 * 携程  价格星级筛选
 * Created by @author Zg on 2018/8/29.
 */

public class CtripPriceModel implements Serializable {

    /**
     * 最低价格
     */
    public String minprice;
    /**
     * 最高价格
     */
    public String maxprice;

    /**
     * 星级id 没有传空字符串 逗号分隔多个星级
     */
    public List<String> starlevel;

    /**
     * 价格星级筛选展示
     */
    public String showStr;


    public CtripPriceModel(String minprice, String maxprice, List<String> starlevel,String showStr) {
        this.minprice = minprice;
        this.maxprice = maxprice;
        this.starlevel = starlevel;
        this.showStr = showStr;
    }

    public CtripPriceModel() {
    }
}

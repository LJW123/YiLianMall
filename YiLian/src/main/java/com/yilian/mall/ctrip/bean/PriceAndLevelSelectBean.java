package com.yilian.mall.ctrip.bean;

import java.util.List;

/**
 * 作者：马铁超 on 2018/11/2 12:03
 */

public class PriceAndLevelSelectBean {
    public String MinPrice;
    public String MaxPrice;
    public List<String> levelId ;
    public String minRange;
    public String maxRange;

    public String getMinPrice() {
        return MinPrice;
    }

    public String getMaxPrice() {
        return MaxPrice;
    }

    public List<String> getLevelId() {
        return levelId;
    }

    public String getMinRange() {
        return minRange;
    }

    public String getMaxRange() {
        return maxRange;
    }

    public void setMinPrice(String minPrice) {
        MinPrice = minPrice;
    }

    public void setMaxPrice(String maxPrice) {
        MaxPrice = maxPrice;
    }

    public void setLevelId(List<String> levelId) {
        this.levelId = levelId;
    }

    public void setMinRange(String minRange) {
        this.minRange = minRange;
    }

    public void setMaxRange(String maxRange) {
        this.maxRange = maxRange;
    }
}

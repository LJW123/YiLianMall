package com.yilian.mall.ctrip.bean;

/**
 * 作者：马铁超 on 2018/8/31 10:32
 */

public class RangeBean {
    private int min_range;
    private int max_range;
    private String rangeName;

    public int getMin_range() {
        return min_range;
    }

    public int getMax_range() {
        return max_range;
    }

    public void setMin_range(int min_range) {
        this.min_range = min_range;
    }

    public void setMax_range(int max_range) {
        this.max_range = max_range;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }
}

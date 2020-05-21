package com.yilian.mall.entity;

/**
 * Created by lauyk on 16/3/20.
 */
public class OrderRouting {

    public String describe;

    public String min;

    public String day;

    public OrderRouting(String describe, String min, String day) {
        this.describe = describe;
        this.min = min;
        this.day = day;
    }
}

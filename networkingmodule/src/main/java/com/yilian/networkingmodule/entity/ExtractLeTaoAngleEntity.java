package com.yilian.networkingmodule.entity;

import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Administrator on 2018/3/3 0003.提取区块益豆到乐淘天使
 */

public class ExtractLeTaoAngleEntity extends HttpResultBean {


    /**
     * bean_amount : 10000
     * actual_amount : 9800
     * charge_amount : 200
     * angel_value : 188
     * angel_amount : 5212
     * time : 1519969669
     * order : 2018030213474962437
     */

    private String bean_amount;
    private String actual_amount;
    private String charge_amount;
    private String angel_value;
    private String angel_amount;
    private String time;
    private String order;

    public String getBean_amount() {
        return bean_amount;
    }

    public void setBean_amount(String bean_amount) {
        this.bean_amount = bean_amount;
    }

    public String getActual_amount() {
        return actual_amount;
    }

    public void setActual_amount(String actual_amount) {
        this.actual_amount = actual_amount;
    }

    public String getCharge_amount() {
        return charge_amount;
    }

    public void setCharge_amount(String charge_amount) {
        this.charge_amount = charge_amount;
    }

    public String getAngel_value() {
        return angel_value;
    }

    public void setAngel_value(String angel_value) {
        this.angel_value = angel_value;
    }

    public String getAngel_amount() {
        return angel_amount;
    }

    public void setAngel_amount(String angel_amount) {
        this.angel_amount = angel_amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}

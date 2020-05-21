package com.yilian.mall.entity;

/**
 * Created by Kent on 2014/12/15.
 */
public class NearDataBase {

    private int item_type = 0;

    public NearDataBase(int item_type) {
        this.item_type = item_type;
    }

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

}

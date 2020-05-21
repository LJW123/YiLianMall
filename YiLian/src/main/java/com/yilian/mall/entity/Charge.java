package com.yilian.mall.entity;

/**
 * Created by Administrator on 2016/3/25.
 */
public class Charge {



    public int price;

    public String descripe;

    public boolean flag;

    public Charge (int price,String descripe,boolean flag){
        this.price = price;
        this.descripe = descripe;
        this.flag = flag;
    }
}

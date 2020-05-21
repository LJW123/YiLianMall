package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class DonationBean extends BaseEntity {

    private int img;
    private String name;

    public DonationBean() {
    }

    public DonationBean(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

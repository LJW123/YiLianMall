package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class TwoPageBean extends BaseEntity {

    private int img;
    private String title;

    public TwoPageBean() {
    }

    public TwoPageBean(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

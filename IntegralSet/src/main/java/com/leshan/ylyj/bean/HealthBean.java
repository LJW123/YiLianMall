package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class HealthBean extends BaseEntity {
    private int img;
    private String title;
    private String diease;
    private String time;


    public HealthBean() {
    }

    public HealthBean(int img, String title, String diease, String time) {
        this.img = img;
        this.title = title;
        this.diease = diease;
        this.time = time;
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

    public String getDiease() {
        return diease;
    }

    public void setDiease(String diease) {
        this.diease = diease;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

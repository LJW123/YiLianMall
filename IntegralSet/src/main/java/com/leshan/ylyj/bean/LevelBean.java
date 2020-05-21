package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class LevelBean extends BaseEntity {

    private String name;
    private int height;
    private String level;

    public LevelBean(String name, int height, String level) {
        this.name = name;
        this.height = height;
        this.level = level;
    }

    public LevelBean() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

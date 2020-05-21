package com.yilian.mall.ctrip.bean;

import java.io.Serializable;

/**
 * 作者：马铁超 on 2018/10/10 14:32
 */

public class FiltrateEventBean implements Serializable{
    private String name;
    private boolean isCheck;
    private int tag;

    public FiltrateEventBean(String name, boolean isCheck, int tag) {
        this.name = name;
        this.isCheck = isCheck;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public int getTag() {
        return tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}

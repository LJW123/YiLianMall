package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class ShareCreditBean extends BaseEntity {
    private String id;
    private String name;
    private String url;


    public ShareCreditBean(String url, String name) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

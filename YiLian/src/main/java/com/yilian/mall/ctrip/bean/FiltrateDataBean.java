package com.yilian.mall.ctrip.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 作者：马铁超 on 2018/10/23 14:40
 */

public class FiltrateDataBean {

    public String filtrateTitle;
    public ArrayList<FiltrateDataBeanContent> dataBeanContents;

    public static class FiltrateDataBeanContent {
        public String id;
        public String name;
        public String key;
        public boolean isCheck;
    }

    public String getFiltrateTitle() {
        return filtrateTitle;
    }

    public void setFiltrateTitle(String filtrateTitle) {
        this.filtrateTitle = filtrateTitle;
    }

    public ArrayList<FiltrateDataBeanContent> getDataBeanContents() {
        return dataBeanContents;
    }

    public void setDataBeanContents(ArrayList<FiltrateDataBeanContent> dataBeanContents) {
        this.dataBeanContents = dataBeanContents;
    }
}

package com.leshan.ylyj.bean;

import java.util.List;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class TreasureBean extends BaseEntity {

    private int type;
    private List<Treasure> data;

    public TreasureBean() {
    }

    public TreasureBean(int type, List<Treasure> data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Treasure> getData() {
        return data;
    }

    public void setData(List<Treasure> data) {
        this.data = data;
    }

    public static class Treasure {
        private int icon;
        private String title;

        public Treasure(int icon, String title) {
            this.icon = icon;
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}

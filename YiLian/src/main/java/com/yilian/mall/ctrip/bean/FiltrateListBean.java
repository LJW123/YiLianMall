package com.yilian.mall.ctrip.bean;

import java.util.ArrayList;

/**
 * 作者：马铁超 on 2018/9/17 09:11
 */

public class FiltrateListBean {
    public static ArrayList<ListItemData> listData;

    public  class ListItemData {
        public String title;
        public boolean isCheck;
        public String key;

        public void setKey(String key) {
            this.key = key;
        }

        public String getTitle() {
            return title;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getKey() {
            return key;
        }
    }
}

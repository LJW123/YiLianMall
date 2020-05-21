package com.yilian.mall.ctrip.bean;

/**
 * 作者：马铁超 on 2018/8/31 18:10
 */

public class FiltrateOneContentBean {

    private String sorts;
    private String title;
    private String comsort;
    private boolean isCheck;


    public String getSorts() {
        return sorts;
    }

    public String getTitle() {
        return title;
    }

    public String getComsort() {
        return comsort;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComsort(String comsort) {
        this.comsort = comsort;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}

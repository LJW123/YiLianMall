package com.yilian.mall.entity;

/**
 * Created by yukun on 2016/4/22.
 */
public class MallMenu {

    public String menuName;

    public String menuDescribe;

    public String photoUrl;

    public String id;

    public int resId;

    public MallMenu(String menuName,String menuDescribe,String photoUrl,int resId) {
        this.menuName = menuName;
        this.menuDescribe = menuDescribe;
        this.photoUrl = photoUrl;
        this.resId = resId;
    }

    public MallMenu(String menuName,String menuDescribe,String photoUrl,int resId,String id) {
        this.menuName = menuName;
        this.menuDescribe = menuDescribe;
        this.photoUrl = photoUrl;
        this.resId = resId;
        this.id = id;
    }


}

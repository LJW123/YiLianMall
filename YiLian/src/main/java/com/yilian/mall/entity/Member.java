package com.yilian.mall.entity;

/**
 * Created by Administrator on 2016/3/29.
 */
public class Member  {
    public String url;

    public String name;

    public String time;

    public String id;
    public Member (String id,String url,String name,String time){
        this.url = url;
        this.name = name;
        this.time = time;
        this.id=id;
    }
}

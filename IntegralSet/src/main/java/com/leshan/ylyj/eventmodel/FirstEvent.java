package com.leshan.ylyj.eventmodel;


import java.util.List;

/**
 * Created by Administrator on 2017/11/25 0025.
 */

public class FirstEvent {

    private String code;
    private int msg;
    private String title;


    public FirstEvent(String code) {
        this.code = code;
    }

    public FirstEvent(String code, int msg) {
        this.code = code;
        this.msg = msg;
    }

    public FirstEvent(String code, String title) {
        this.code = code;
        this.title = title;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

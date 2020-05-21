package com.yilian.mylibrary.widget.jdaddressseletor.areamodel;

/**
 * @author Created by  on 2018/5/25.
 */

public class JDProvince extends JDArea{
    @Override
    public String toString() {
        return "JDProvince{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public JDProvince(String name, String id) {
        this.name = name;
        this.id = id;


    }
}

package com.leshan.ylyj.bean;


import com.yiguo.adressselectorlib.CityInterface;

public class CityBean implements CityInterface {
    private String Name;
    private String id;
    private String Grade;
    private String IsMunicipality;

    public String getIsMunicipality() {
        return IsMunicipality;
    }

    public void setIsMunicipality(String isMunicipality) {
        IsMunicipality = isMunicipality;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    @Override
    public String getCityName() {
        return Name;
    }
}

package com.yilian.mall.ctrip.bean;

import java.io.Serializable;

/**
 * 作者：马铁超 on 2018/10/31 11:00
 */

public class MapHotelInfoBean implements Serializable{
     public String hotelName;
     public String hotelInfo;
     public String hotelGrade;
     public String hotelId;
     public Double gdLat;
     public Double gdLong;
     public String checkIn;
     public String checkOut;
     public String addRess;
    public String getHotelName() {
        return hotelName;
    }

    public String getHotelInfo() {
        return hotelInfo;
    }

    public String getHotelGrade() {
        return hotelGrade;
    }

    public String getHotelId() {
        return hotelId;
    }

    public Double getGdLat() {
        return gdLat;
    }

    public Double getGdLong() {
        return gdLong;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setHotelInfo(String hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public void setHotelGrade(String hotelGrade) {
        this.hotelGrade = hotelGrade;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void setGdLat(Double gdLat) {
        this.gdLat = gdLat;
    }

    public void setGdLong(Double gdLong) {
        this.gdLong = gdLong;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}

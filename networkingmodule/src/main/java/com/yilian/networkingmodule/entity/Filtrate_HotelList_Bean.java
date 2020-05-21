package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：马铁超 on 2018/9/8 10:24
 */

public class Filtrate_HotelList_Bean implements Serializable{


    @SerializedName("location")
    private int location;
    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataBean> data;

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {


        private String HotelID;
        private String HotelName;
        private String StarRatingDec;
        private String AdjacentIntersection;
        private String CtripUserRating;
        private String CtripUserRatingsDec;
        private String image;
        private String ThemesName;
        private String MinPrice;
        private String returnBean;
        private String gdLng;
        private String gdLat;
        private String Distance;

        public String getHotelID() {
            return HotelID;
        }

        public void setHotelID(String HotelID) {
            this.HotelID = HotelID;
        }

        public String getHotelName() {
            return HotelName;
        }

        public void setHotelName(String HotelName) {
            this.HotelName = HotelName;
        }

        public String getStarRatingDec() {
            return StarRatingDec;
        }

        public void setStarRatingDec(String StarRatingDec) {
            this.StarRatingDec = StarRatingDec;
        }

        public String getAdjacentIntersection() {
            return AdjacentIntersection;
        }

        public void setAdjacentIntersection(String AdjacentIntersection) {
            this.AdjacentIntersection = AdjacentIntersection;
        }

        public String getCtripUserRating() {
            return CtripUserRating;
        }

        public void setCtripUserRating(String CtripUserRating) {
            this.CtripUserRating = CtripUserRating;
        }

        public String getCtripUserRatingsDec() {
            return CtripUserRatingsDec;
        }

        public void setCtripUserRatingsDec(String CtripUserRatingsDec) {
            this.CtripUserRatingsDec = CtripUserRatingsDec;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getThemesName() {
            return ThemesName;
        }

        public void setThemesName(String ThemesName) {
            this.ThemesName = ThemesName;
        }

        public String getMinPrice() {
            return MinPrice;
        }

        public void setMinPrice(String MinPrice) {
            this.MinPrice = MinPrice;
        }

        public String getReturnBean() {
            return returnBean;
        }

        public void setReturnBean(String returnBean) {
            this.returnBean = returnBean;
        }

        public String getGdLng() {
            return gdLng;
        }

        public void setGdLng(String gdLng) {
            this.gdLng = gdLng;
        }

        public String getGdLat() {
            return gdLat;
        }

        public void setGdLat(String gdLat) {
            this.gdLat = gdLat;
        }

        public String getDistance() {
            return Distance;
        }

        public void setDistance(String Distance) {
            this.Distance = Distance;
        }
    }
}

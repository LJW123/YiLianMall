package com.yilian.mylibrary.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/19.
 */
public class UserAddressLists implements Serializable{
    public String address_id;//收货地址id
    public String province_id;//省id
    public String province_name;//省名称
    public String city_id;//市id
    public String city_name;//市名称
    public String county_id;//区县id
    public String county_name;//区县名称
    public String address;//详细地址
    public String phone;//电话
    public String contacts;//收货人
    public int default_address;//0不是默认 1默认


    @SerializedName("lat")
    public String latitude;
    @SerializedName("lng")
    public String longitude;
    @SerializedName("full_address")
    public String fullAddress;//新增字段，即设置地址时的“小区/大厦/学校”
    @SerializedName("is_new")
    public String isNew;//新增字段，//经纬度是否设置0未设置1已设置

    @SerializedName("has_card") //是否有身份证信息 0没有 ，1 有
    public String hasCard;
    @SerializedName("identity_name") //身份证姓名
    public String identityName;
    @SerializedName("identity_id") //身份证号
    public String identityId;
    @SerializedName("identity_front") //身份证正面
    public String identityFront;
    @SerializedName("identity_back") //身份证反面
    public String identityBack;

    @SerializedName("is_default") //新增字段 是否是默认地址
    public String is_Default;

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setDefault_address(int default_address) {
        this.default_address = default_address;
    }

    @Override
    public String toString() {
        return "UserAddressLists{" +
                "address_id='" + address_id + '\'' +
                ", province_id='" + province_id + '\'' +
                ", province_name='" + province_name + '\'' +
                ", city_id='" + city_id + '\'' +
                ", city_name='" + city_name + '\'' +
                ", county_id='" + county_id + '\'' +
                ", county_name='" + county_name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", contacts='" + contacts + '\'' +
                ", default_address=" + default_address +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", isNew='" + isNew + '\'' +
                ", hasCard='" + hasCard + '\'' +
                ", identityName='" + identityName + '\'' +
                ", identityId='" + identityId + '\'' +
                ", identityFront='" + identityFront + '\'' +
                ", identityBack='" + identityBack + '\'' +
                ", is_Default='" + is_Default + '\'' +
                '}';
    }
}

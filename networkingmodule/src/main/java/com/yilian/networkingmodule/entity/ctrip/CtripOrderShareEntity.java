package com.yilian.networkingmodule.entity.ctrip;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 跳转分享 传递实体类
 *
 * @author Created by Zg on 2018/7/25.
 */

public class CtripOrderShareEntity implements Parcelable {


    public static final Creator<CtripOrderShareEntity> CREATOR = new Creator<CtripOrderShareEntity>() {
        @Override
        public CtripOrderShareEntity createFromParcel(Parcel in) {
            return new CtripOrderShareEntity(in);
        }

        @Override
        public CtripOrderShareEntity[] newArray(int size) {
            return new CtripOrderShareEntity[size];
        }
    };

    private String id;
    //酒店id
    private String hotelId;
    //订单价
    private String InclusiveAmount;
    // 入住开始时间
    private String TimeSpanStart;
    // 入住结束时间
    private String TimeSpanEnd;
    // 预定房间数
    private String NumberOfUnits;
    //入住晚数
    private String nightNum;
    // 酒店名字
    private String HotelName;
    //酒店地址
    private String Address;
    //酒店主图
    private String image;
    //酒店房型起价
    private String MinPrice;
    //用户评分
    private String CtripUserRating;

    public CtripOrderShareEntity(Parcel in) {
        id = in.readString();
        hotelId = in.readString();
        InclusiveAmount = in.readString();
        TimeSpanStart = in.readString();
        TimeSpanEnd = in.readString();
        NumberOfUnits = in.readString();
        nightNum = in.readString();
        HotelName = in.readString();
        Address = in.readString();
        image = in.readString();
        MinPrice = in.readString();
        CtripUserRating = in.readString();
    }

    public CtripOrderShareEntity() {

    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotelId() {
        return hotelId == null ? "" : hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getInclusiveAmount() {
        return InclusiveAmount == null ? "" : InclusiveAmount;
    }

    public void setInclusiveAmount(String inclusiveAmount) {
        InclusiveAmount = inclusiveAmount;
    }

    public String getTimeSpanStart() {
        return TimeSpanStart == null ? "" : TimeSpanStart;
    }

    public void setTimeSpanStart(String timeSpanStart) {
        TimeSpanStart = timeSpanStart;
    }

    public String getTimeSpanEnd() {
        return TimeSpanEnd == null ? "" : TimeSpanEnd;
    }

    public void setTimeSpanEnd(String timeSpanEnd) {
        TimeSpanEnd = timeSpanEnd;
    }

    public String getNumberOfUnits() {
        return NumberOfUnits == null ? "" : NumberOfUnits;
    }

    public void setNumberOfUnits(String numberOfUnits) {
        NumberOfUnits = numberOfUnits;
    }

    public String getNightNum() {
        return nightNum == null ? "" : nightNum;
    }

    public void setNightNum(String nightNum) {
        this.nightNum = nightNum;
    }

    public String getHotelName() {
        return HotelName == null ? "" : HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getAddress() {
        return Address == null ? "" : Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMinPrice() {
        return MinPrice == null ? "" : MinPrice;
    }

    public void setMinPrice(String minPrice) {
        MinPrice = minPrice;
    }

    public String getCtripUserRating() {
        return CtripUserRating == null ? "" : CtripUserRating;
    }

    public void setCtripUserRating(String ctripUserRating) {
        CtripUserRating = ctripUserRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(hotelId);
        dest.writeString(InclusiveAmount);
        dest.writeString(TimeSpanStart);
        dest.writeString(TimeSpanEnd);
        dest.writeString(NumberOfUnits);
        dest.writeString(nightNum);
        dest.writeString(HotelName);
        dest.writeString(Address);
        dest.writeString(image);
        dest.writeString(MinPrice);
        dest.writeString(CtripUserRating);
    }
}

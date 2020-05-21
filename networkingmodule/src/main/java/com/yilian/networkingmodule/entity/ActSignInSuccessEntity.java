package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2017/11/21 0021.
 */

public class ActSignInSuccessEntity extends HttpResultBean implements Parcelable{

    /**
     code: 1,
     msg: "签到成功~",
     integral: 1,    本次签到获得奖券
     days: 1         连续签到天数
     */

    @SerializedName("integral")
    public String integral;
    @SerializedName("days")
    public int days;
    @SerializedName("weekIntegral")
    public int weekIntegral;

    protected ActSignInSuccessEntity(Parcel in) {
        integral = in.readString();
        days = in.readInt();
        weekIntegral = in.readInt();
    }

    public static final Creator<ActSignInSuccessEntity> CREATOR = new Creator<ActSignInSuccessEntity>() {
        @Override
        public ActSignInSuccessEntity createFromParcel(Parcel in) {
            return new ActSignInSuccessEntity(in);
        }

        @Override
        public ActSignInSuccessEntity[] newArray(int size) {
            return new ActSignInSuccessEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(integral);
        parcel.writeInt(days);
        parcel.writeInt(weekIntegral);
    }
}

package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/8/9 0009.
 */

public class MerchantCashSuccessEntity extends HttpResultBean implements Parcelable{


    /**
     * card_bank : 招商银行
     * card_number : 123456
     * time : 1502266120
     * cash_amount : 10000
     */

    @SerializedName("card_bank")
    public String cardBank;
    @SerializedName("card_number")
    public String cardNumber;
    @SerializedName("time")
    public long time;
    @SerializedName("cash_amount")
    public String cashAmount;

    protected MerchantCashSuccessEntity(Parcel in) {
        cardBank = in.readString();
        cardNumber = in.readString();
        time = in.readLong();
        cashAmount = in.readString();
    }

    public static final Creator<MerchantCashSuccessEntity> CREATOR = new Creator<MerchantCashSuccessEntity>() {
        @Override
        public MerchantCashSuccessEntity createFromParcel(Parcel in) {
            return new MerchantCashSuccessEntity(in);
        }

        @Override
        public MerchantCashSuccessEntity[] newArray(int size) {
            return new MerchantCashSuccessEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardBank);
        dest.writeString(cardNumber);
        dest.writeLong(time);
        dest.writeString(cashAmount);
    }
}

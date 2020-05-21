package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ZYH on 2017/12/6 0006.
 */

public class SendPackgeRecorder extends HttpResultBean {


    /**
     * data : {"merchant_name":"测试商家","merchant_image":"app/images/mall/20170918/6291685602586311_6545359_image.jpg","amount":"500","over_amount":500,"unlock_amount":"0","list":[{"id":"4","bonus_id":"1","be_user_id":"0","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235765","deadline_at":"1513978420","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"},{"id":"3","bonus_id":"1","be_user_id":"0","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235760","deadline_at":"1513840560","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"},{"id":"2","bonus_id":"1","be_user_id":"0","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235754","deadline_at":"1513840554","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"},{"id":"1","bonus_id":"1","be_user_id":"6308797466188805","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235715","deadline_at":"1513840515","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"}]}
     */

    @SerializedName("data")
    public Data data;

    public static class Data {
        /**
         * merchant_name : 测试商家
         * merchant_image : app/images/mall/20170918/6291685602586311_6545359_image.jpg
         * amount : 500
         * over_amount : 500
         * unlock_amount : 0
         * list : [{"id":"4","bonus_id":"1","be_user_id":"0","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235765","deadline_at":"1513978420","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"},{"id":"3","bonus_id":"1","be_user_id":"0","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235760","deadline_at":"1513840560","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"},{"id":"2","bonus_id":"1","be_user_id":"0","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235754","deadline_at":"1513840554","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"},{"id":"1","bonus_id":"1","be_user_id":"6308797466188805","user_id":"6337951144236713","amount":"10","last_amount":"10","apply_at":"1513235715","deadline_at":"1513840515","status":"0","is_pushed":"2","name":"孟俊锋","photo":"app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg"}]
         */

        @SerializedName("merchant_name")
        public String merchantName;
        @SerializedName("merchant_image")
        public String merchantImage;
        @SerializedName("amount")
        public String amount;
        @SerializedName("over_amount")
        public int overAmount;
        @SerializedName("unlock_amount")
        public String unlockAmount;
        @SerializedName("list")
        public java.util.List<RecordDetails> list;

        public static class RecordDetails implements Parcelable{
            /**
             * id : 4
             * bonus_id : 1
             * be_user_id : 0
             * user_id : 6337951144236713
             * amount : 10
             * last_amount : 10
             * apply_at : 1513235765
             * deadline_at : 1513978420
             * status : 0
             * is_pushed : 2
             * name : 孟俊锋
             * photo : app/images/head/20171220/6337951144236713_2738144_1513748437906environment.jpg
             */

            @SerializedName("id")
            public String id;
            @SerializedName("bonus_id")
            public String bonusId;
            @SerializedName("be_user_id")
            public String beUserId;
            @SerializedName("user_id")
            public String userId;
            @SerializedName("amount")
            public String amount;
            @SerializedName("last_amount")
            public String lastAmount;
            @SerializedName("apply_at")
            public String applyAt;
            @SerializedName("deadline_at")
            public String deadlineAt;
            @SerializedName("status")
            public String status;
            @SerializedName("is_pushed")
            public String isPushed;
            @SerializedName("name")
            public String name;
            @SerializedName("photo")
            public String photo;

            protected RecordDetails(Parcel in) {
                id = in.readString();
                bonusId = in.readString();
                beUserId = in.readString();
                userId = in.readString();
                amount = in.readString();
                lastAmount = in.readString();
                applyAt = in.readString();
                deadlineAt = in.readString();
                status = in.readString();
                isPushed = in.readString();
                name = in.readString();
                photo = in.readString();
            }

            public static final Creator<RecordDetails> CREATOR = new Creator<RecordDetails>() {
                @Override
                public RecordDetails createFromParcel(Parcel in) {
                    return new RecordDetails(in);
                }

                @Override
                public RecordDetails[] newArray(int size) {
                    return new RecordDetails[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(id);
                parcel.writeString(bonusId);
                parcel.writeString(beUserId);
                parcel.writeString(userId);
                parcel.writeString(amount);
                parcel.writeString(lastAmount);
                parcel.writeString(applyAt);
                parcel.writeString(deadlineAt);
                parcel.writeString(status);
                parcel.writeString(isPushed);
                parcel.writeString(name);
                parcel.writeString(photo);
            }
        }
    }
}

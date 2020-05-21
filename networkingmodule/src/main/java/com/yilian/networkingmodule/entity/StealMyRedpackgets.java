package com.yilian.networkingmodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.List;

/**
 * Created by ZYH on 2017/10/13.
 */

public class StealMyRedpackgets extends HttpResultBean {


    /**
     * code : 1
     * msg : success
     * data : {"list":[{"merchant_id":"","merchant_name":"","merchant_latitude":"","merchant_longitude":"","merchant_image":"","count":""},{}],"emp_list":[{"merchant_id":"","merchant_name":"","merchant_latitude":"","merchant_longitude":"","merchant_image":"","count":""},{}]}
     */
    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("list")
        public java.util.List<MyPackges> list;//我的奖励
        @SerializedName("emp_list")
        public java.util.List<MoreRedDetails> empList;//更多奖励


        public static class MyPackges extends RedDetails {
            protected MyPackges(Parcel in) {
                super(in);
            }
        }

        public static class MoreRedDetails extends RedDetails {
            protected MoreRedDetails(Parcel in) {
                super(in);
            }
        }

        public static class RedDetails implements Parcelable {
            /**
             * merchant_id :
             * merchant_name :
             * merchant_latitude :
             * merchant_longitude :
             * merchant_image :
             * count :
             */
            @SerializedName("merchant_id")
            public String merchantId;
            @SerializedName("merchant_name")
            public String merchantName;
            @SerializedName("merchant_latitude")
            public String merchantLatitude;
            @SerializedName("merchant_longitude")
            public String merchantLongitude;
            @SerializedName("merchant_image")
            public String merchantImage;
            @SerializedName("count")
            public String count;
            @SerializedName("distance")
            public String distance;
            @SerializedName("last_amount")
            public String lastAmount;

            protected RedDetails(Parcel in) {
                merchantId = in.readString();
                merchantName = in.readString();
                merchantLatitude = in.readString();
                merchantLongitude = in.readString();
                merchantImage = in.readString();
                count = in.readString();
                distance = in.readString();
                lastAmount = in.readString();
            }

            public static final Creator<RedDetails> CREATOR = new Creator<RedDetails>() {
                @Override
                public RedDetails createFromParcel(Parcel in) {
                    return new RedDetails(in);
                }

                @Override
                public RedDetails[] newArray(int size) {
                    return new RedDetails[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(merchantId);
                parcel.writeString(merchantName);
                parcel.writeString(merchantLatitude);
                parcel.writeString(merchantLongitude);
                parcel.writeString(merchantImage);
                parcel.writeString(count);
                parcel.writeString(distance);
                parcel.writeString(lastAmount);
            }
        }

    }
}

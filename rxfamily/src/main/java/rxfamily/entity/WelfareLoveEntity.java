package rxfamily.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import rxfamily.entity.BaseEntity;

/**
 *
 * 网络的请求实体类，必须继承-rxfamily.entity.BaseEntity
 * @author zhaiyaohua on 2018/1/15 0015.
 */

public class WelfareLoveEntity extends BaseEntity   {

    /**
     * data : {"total":3}
     * desc : 执行成功
     */

    @SerializedName("data")
    public Data data;
    @SerializedName("desc")
    public String desc;

    protected WelfareLoveEntity(Parcel in) {
        desc = in.readString();
    }



    public static class Data  implements Parcelable{
        /**
         * total : 3
         */

        @SerializedName("total")
        public String total;

        protected Data(Parcel in) {
            total = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(total);
        }
    }
}

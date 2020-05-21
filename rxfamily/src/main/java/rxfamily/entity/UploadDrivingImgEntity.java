package rxfamily.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author zyh
 */

public class UploadDrivingImgEntity extends BaseEntity implements Parcelable {


    /**
     * data : {"picUrl":"http://oss-cn-beijing.aliyuncs.com/lfyilian/npm/credit/f27f312ba0c1498c961309c097cef32e.png"}
     * datas : null
     * desc : null
     * outputPage : null
     */

    @SerializedName("data")
    public Data data;
    @SerializedName("datas")
    public Object datas;
    @SerializedName("desc")
    public Object desc;
    @SerializedName("outputPage")
    public Object outputPage;

    protected UploadDrivingImgEntity(Parcel in) {
        data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<UploadDrivingImgEntity> CREATOR = new Creator<UploadDrivingImgEntity>() {
        @Override
        public UploadDrivingImgEntity createFromParcel(Parcel in) {
            return new UploadDrivingImgEntity(in);
        }

        @Override
        public UploadDrivingImgEntity[] newArray(int size) {
            return new UploadDrivingImgEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(data, i);
    }

    public static class Data implements Parcelable {
        /**
         * picUrl : http://oss-cn-beijing.aliyuncs.com/lfyilian/npm/credit/f27f312ba0c1498c961309c097cef32e.png
         */

        @SerializedName("picUrl")
        public String picUrl;

        protected Data(Parcel in) {
            picUrl = in.readString();
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
            parcel.writeString(picUrl);
        }
    }
}

package rxfamily.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by @author zhaiyaohua on 2018/1/22 0022.
 */

public class DrivingLicenseOutputValueEntity implements Parcelable{

    /**
     * config_str :
     * engine_num :
     * issue_date :
     * model :
     * owner :
     * plate_num :
     * register_date :
     * request_id : 20180122210949_9b0d268447f7e21b1a603a2a031cc53c
     * success : true
     * vehicle_type :
     * vin :
     */

    @SerializedName("config_str")
    public String configStr;
    @SerializedName("engine_num")
    public String engineNum;
    @SerializedName("issue_date")
    public String issueDate;
    @SerializedName("model")
    public String model;
    @SerializedName("owner")
    public String owner;
    @SerializedName("plate_num")
    public String plateNum;
    @SerializedName("register_date")
    public String registerDate;
    @SerializedName("request_id")
    public String requestId;
    @SerializedName("success")
    public boolean success;
    @SerializedName("vehicle_type")
    public String vehicleType;
    @SerializedName("vin")
    public String vin;

    protected DrivingLicenseOutputValueEntity(Parcel in) {
        configStr = in.readString();
        engineNum = in.readString();
        issueDate = in.readString();
        model = in.readString();
        owner = in.readString();
        plateNum = in.readString();
        registerDate = in.readString();
        requestId = in.readString();
        success = in.readByte() != 0;
        vehicleType = in.readString();
        vin = in.readString();
    }

    public static final Creator<DrivingLicenseOutputValueEntity> CREATOR = new Creator<DrivingLicenseOutputValueEntity>() {
        @Override
        public DrivingLicenseOutputValueEntity createFromParcel(Parcel in) {
            return new DrivingLicenseOutputValueEntity(in);
        }

        @Override
        public DrivingLicenseOutputValueEntity[] newArray(int size) {
            return new DrivingLicenseOutputValueEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(configStr);
        parcel.writeString(engineNum);
        parcel.writeString(issueDate);
        parcel.writeString(model);
        parcel.writeString(owner);
        parcel.writeString(plateNum);
        parcel.writeString(registerDate);
        parcel.writeString(requestId);
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeString(vehicleType);
        parcel.writeString(vin);
    }
   }

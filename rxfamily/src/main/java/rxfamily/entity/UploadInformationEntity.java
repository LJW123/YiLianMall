package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class UploadInformationEntity extends BaseEntity {

    @SerializedName("desc")
    public String desc;
    @SerializedName("datas")
    public String datas;
    @SerializedName("outputPage")
    public String outputPage;

    @SerializedName("data")
    public DataBean data;

    public class DataBean{

        /**
         * driverStatus : 1
         * educationStatus : 1
         * vehicleStatus : 1
         * emailStatus:1  1已认证0未认证
         * realNameStatus   是否实名认证
         */

        @SerializedName("driverStatus")
        private String driverStatus;
        @SerializedName("educationStatus")
        private String educationStatus;
        @SerializedName("vehicleStatus")
        private String vehicleStatus;
        @SerializedName("emailStatus")
        private String emailStatus;
        @SerializedName("realNameStatus")
        private String realNameStatus;

        public String getEmailStatus() {
            return emailStatus;
        }

        public void setEmailStatus(String emailStatus) {
            this.emailStatus = emailStatus;
        }

        public String getRealNameStatus() {
            return realNameStatus;
        }

        public void setRealNameStatus(String realNameStatus) {
            this.realNameStatus = realNameStatus;
        }

        public String getDriverStatus() {
            return driverStatus;
        }

        public void setDriverStatus(String driverStatus) {
            this.driverStatus = driverStatus;
        }

        public String getEducationStatus() {
            return educationStatus;
        }

        public void setEducationStatus(String educationStatus) {
            this.educationStatus = educationStatus;
        }

        public String getVehicleStatus() {
            return vehicleStatus;
        }

        public void setVehicleStatus(String vehicleStatus) {
            this.vehicleStatus = vehicleStatus;
        }
    }

}

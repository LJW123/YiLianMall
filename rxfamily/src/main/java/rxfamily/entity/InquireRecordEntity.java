package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class InquireRecordEntity extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean{

        /**
         * area_id : 1
         * area_name : 河南
         * college_id : 1
         * college_name : 北大
         * current_status : 1
         * education : 1
         * enrol_time : 2017-01
         * graduate_time : 2017-01
         * id : 15c4a4997aee4bdbbcb82a8ecb9bb8b5
         * user_id : 15c4a4997aee4bdbbcb82a8ecb9bb8b5
         */

        @SerializedName("area_id")
        private String areaId;
        @SerializedName("area_name")
        private String areaName;
        @SerializedName("college_id")
        private String collegeId;
        @SerializedName("college_name")
        private String collegeName;
        @SerializedName("current_status")
        private int currentStatus;
        @SerializedName("education")
        private int education;
        @SerializedName("enrol_time")
        private String enrolTime;
        @SerializedName("graduate_time")
        private String graduateTime;
        @SerializedName("id")
        private String id;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("create_time")
        private String create_time;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getCollegeId() {
            return collegeId;
        }

        public void setCollegeId(String collegeId) {
            this.collegeId = collegeId;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }

        public int getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(int currentStatus) {
            this.currentStatus = currentStatus;
        }

        public int getEducation() {
            return education;
        }

        public void setEducation(int education) {
            this.education = education;
        }

        public String getEnrolTime() {
            return enrolTime;
        }

        public void setEnrolTime(String enrolTime) {
            this.enrolTime = enrolTime;
        }

        public String getGraduateTime() {
            return graduateTime;
        }

        public void setGraduateTime(String graduateTime) {
            this.graduateTime = graduateTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

}

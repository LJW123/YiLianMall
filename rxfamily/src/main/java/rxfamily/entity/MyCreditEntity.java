package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class MyCreditEntity extends BaseEntity {
    /**
     * 信用最高值
     */
    public static final int MAX_CREDIT_SCORE = 1000;
    @SerializedName("data")
    public MyCreditEntity.DataBean data;

    public class DataBean {
        @SerializedName("myCredit")
        public myCredit dataCredit;
        @SerializedName("myCreditPercentage")
        public myCreditPercentage dataCreditPercentage;


    }

    public class myCredit {

        /**
         * "assets": 3,//资产评分
         * "behaviour": 1,//行为评分
         * "credit": 4,//信用评分
         * "creditTotal": 22,// 信用总分
         * "creditCeiling": 1000,// 信用上限
         * "creditLevel": 1,// 信用等级
         * "creditLevelName": "暂无评分",// 信用等级名称
         * "identity": 2,//身份特质评分
         * "networking": 5,//人脉
         * "tempCredit": 7,//临时信用
         * "isAgree": 1,// 同意协议状态 1同意0不同意
         * "update_time": "2018-01-01",//评估时间
         * "user_id": "123"
         * //* "creditAgreement": 7,//协议内容
         */
        @SerializedName("creditAgreement")
        public String creditAgreement;

        @SerializedName("assets")
        private int assets;
        @SerializedName("behaviour")
        private int behaviour;
        @SerializedName("credit")
        private int credit;
        @SerializedName("creditTotal")
        private int creditTotal;
        @SerializedName("identity")
        private int identity;
        @SerializedName("networking")
        private int networking;
        @SerializedName("tempCredit")
        private int tempCredit;
        @SerializedName("update_time")
        private String updateTime;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("creditCeiling")
        private int creditCeiling;
        @SerializedName("creditLevelName")
        private String creditLevelName;
        @SerializedName("isAgree")
        private int isAgree;


        public String getCreditLevelName() {
            return creditLevelName;
        }

        public void setCreditLevelName(String creditLevelName) {
            this.creditLevelName = creditLevelName;
        }

        public int getCreditCeiling() {
            return creditCeiling;
        }

        public void setCreditCeiling(int creditCeiling) {
            this.creditCeiling = creditCeiling;
        }

        public int getAssets() {
            return assets;
        }

        public void setAssets(int assets) {
            this.assets = assets;
        }

        public int getBehaviour() {
            return behaviour;
        }

        public void setBehaviour(int behaviour) {
            this.behaviour = behaviour;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public int getCreditTotal() {
            if (creditTotal > MAX_CREDIT_SCORE) {
                creditTotal = MAX_CREDIT_SCORE;
            }
            return creditTotal;
        }

        public void setCreditTotal(int creditTotal) {
            this.creditTotal = creditTotal;
        }

        public int getIdentity() {
            return identity;
        }

        public void setIdentity(int identity) {
            this.identity = identity;
        }

        public int getNetworking() {
            return networking;
        }

        public void setNetworking(int networking) {
            this.networking = networking;
        }

        public int getTempCredit() {
            return tempCredit;
        }

        public void setTempCredit(int tempCredit) {
            this.tempCredit = tempCredit;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIsAgree() {
            return isAgree;
        }

        public void setIsAgree(int isAgree) {
            this.isAgree = isAgree;
        }
    }

    public class myCreditPercentage {

        /**
         * "creditPercentage": "80%",// 信用百分比
         * "userID": "123"
         */

        @SerializedName("creditPercentage")
        private String creditPercentage;
        @SerializedName("userID")
        private String userID;

        public String getCreditPercentage() {
            return creditPercentage;
        }

        public void setCreditPercentage(String creditPercentage) {
            this.creditPercentage = creditPercentage;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }
    }
}

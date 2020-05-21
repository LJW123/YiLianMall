package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 集分宝模块 转出转入状态
 * Created by Administrator on 2018/1/13 0013.
 */

public class IntegralStatusEntity extends BaseEntity {


    @SerializedName("data")//记录
    public Data data;

    public class Data implements Serializable {

        /**
         * {
         * server_time:'',//服务器时间
         * amount:'10000',//转出金额 分
         * fee_percent:''//手续费百分比  10就是10%
         * }
         */

        @SerializedName("server_time")
        private String serverTime;
        @SerializedName("amount")
        private String amount;
        @SerializedName("fee_percent")
        private String feePercent;

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFeePercent() {
            return feePercent;
        }

        public void setFeePercent(String feePercent) {
            this.feePercent = feePercent;
        }
    }

}

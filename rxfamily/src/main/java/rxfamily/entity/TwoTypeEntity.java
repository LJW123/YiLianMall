package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class TwoTypeEntity extends BaseEntity {


    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {

        /**
         * bank_name : 中国工商银行
         * bank_logo : /app/images/bank/20170617/工商银行.png
         */
        @SerializedName("bank_name")
        private String bankName;
        @SerializedName("bank_logo")
        private String bankLogo;


        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankLogo() {
            return bankLogo;
        }

        public void setBankLogo(String bankLogo) {
            this.bankLogo = bankLogo;
        }
    }
}

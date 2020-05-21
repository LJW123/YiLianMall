package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class ShortcutDonateEntity extends BaseEntity {

    @SerializedName("datas")
    public String datas;
    @SerializedName("desc")
    public String desc;
    @SerializedName("outputPage")
    public String outputPage;
    @SerializedName("data")
    public DataBean data;

    public class DataBean{
        @SerializedName("creditProportion")
        public int creditProportion;
        @SerializedName("integralList")
        public List<LintegralListBean> integralList;
    }
    public class LintegralListBean{

        /**
         * "integral": 20,//捐赠奖券数量
         *"integralCredit": 2000//获取信用值
         */

        @SerializedName("integral")
        private int integral;
        @SerializedName("integralCredit")
        private int integralCredit;

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getIntegralCredit() {
            return integralCredit;
        }

        public void setIntegralCredit(int integralCredit) {
            this.integralCredit = integralCredit;
        }
    }

}

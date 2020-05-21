package rxfamily.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class MyHealthTopMsgBean extends BaseEntity {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {
        @SerializedName("APPLES_NUM")//当前剩余健康果总数
        public String APPLES_NUM;
        @SerializedName("AMOUNT") //互助总金额
        public String AMOUNT;
        @SerializedName("JOIN_NUM")//已参与人数
        public String JOIN_NUM;
        @SerializedName("total")
        public String total;
    }
}

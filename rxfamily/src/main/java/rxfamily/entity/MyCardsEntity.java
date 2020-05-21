package rxfamily.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class MyCardsEntity extends BaseEntity {


    /**
     * code : 1
     * data : [{"card_index":"1","card_bank":"中国银行","card_type":"0","card_type_cn":"储蓄卡","card_number":"5274123132123132123123132","bank_logo":"app/images/logo.jpg","account_type":""},{},{},{}]
     */

    @SerializedName("code")
    private String codeX;
    private List<DataBean> data;

    public static class DataBean {

        /**
         * card_index : 1
         * card_bank : 中国银行
         * card_type : 0
         * card_type_cn : 储蓄卡
         * card_number : 5274123132123132123123132
         * bank_logo : app/images/logo.jpg
         * account_type :
         */
        @SerializedName("card_index")
        private String card_index;
        @SerializedName("card_bank")
        private String card_bank;
        @SerializedName("card_type")
        private String card_type;
        @SerializedName("card_type_cn")
        private String card_type_cn;
        @SerializedName("card_number")
        private String card_number;
        @SerializedName("bank_logo")
        private String bank_logo;
        @SerializedName("account_type")
        private String account_type;

    }
}

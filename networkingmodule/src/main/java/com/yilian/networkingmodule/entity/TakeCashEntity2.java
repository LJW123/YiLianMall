package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @author Created by  on 2018/1/24.
 *         新版默认提现银行卡实体类
 */

public class TakeCashEntity2 extends HttpResultBean {

    /**
     * available_cash : 96190000
     * info : {"card_index":"145","bank_logo":"/app/images/bank/20170617/交通银行.png","card_type":"0","card_type_cn":"储蓄卡","card_bank":"交通银行","card_number":"2529"}
     */

    @SerializedName("available_cash")
    public String availableCash;
    @SerializedName("info")
    public InfoBean info;

    public static class InfoBean {
        /**
         * card_index : 145
         * bank_logo : /app/images/bank/20170617/交通银行.png
         * card_type : 0
         * card_type_cn : 储蓄卡
         * card_bank : 交通银行
         * card_number : 2529
         */

        @SerializedName("card_index")
        public String cardIndex;
        @SerializedName("bank_logo")
        public String bankLogo;
        @SerializedName("card_type")
        public String cardType;
        @SerializedName("card_type_cn")
        public String cardTypeCn;
        @SerializedName("card_bank")
        public String cardBank;
        @SerializedName("card_number")
        public String cardNumber;
    }
}

package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2018/1/13 0013.
 */

public class CardMyBankCardsListEntity extends HttpResultBean {
    /**
     * card_index: "1",//银行卡唯一标识   如果为0则为老卡 需要重新走添加银行卡校验流程
     * card_bank: "中国银行",//银行名称
     * card_type: "0",//0储蓄卡  1信用卡
     * card_type_cn: "储蓄卡",//卡类型中文
     * card_number: '**** **** **** ***2 529',//处理过的卡号
     * bank_logo: 'app/images/logo.jpg'//卡的logo
     * account_type:"",//0 私人账户 1对公账户
     *  "card_number_show": "6222620620027345629", 完整的卡号
     */
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        @SerializedName("card_number_show")
        public String cardNumberShow;
        @SerializedName("card_index")
        public String carIindex;
        @SerializedName("card_bank")
        public String cardBank;
        @SerializedName("card_type")
        public String cardType;
        @SerializedName("card_number")
        public String cardNumber;
        @SerializedName("bank_logo")
        public String bankLogo;
        @SerializedName("account_type")
        public int accountType;
    }
}

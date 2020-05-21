package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ray_L_Pain on 2017/8/9 0009.
 */

public class CashSuccessModel extends BaseEntity {


    /**
     * card_bank : 招商银行
     * card_number : 123456
     * time : 1502266120
     * cash_amount : 10000
     */

    @SerializedName("card_bank")
    public String cardBank;
    @SerializedName("card_number")
    public String cardNumber;
    @SerializedName("time")
    public String time;
    @SerializedName("cash_amount")
    public String cashAmount;
}

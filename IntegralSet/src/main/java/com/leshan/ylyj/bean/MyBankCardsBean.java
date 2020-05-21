package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class MyBankCardsBean extends BaseEntity {

    private String bank;
    private String cardtype;
    private String cardnumber;
    private String imgurl;

    public MyBankCardsBean(String bank, String cardtype, String cardnumber, String imgurl) {
        this.bank = bank;
        this.cardtype = cardtype;
        this.cardnumber = cardnumber;
        this.imgurl = imgurl;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}

package com.leshan.ylyj.bean;

import rxfamily.entity.BaseEntity;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class DonationIntegralBean extends BaseEntity {
    private String integral;
    private String creditscore;


    public DonationIntegralBean(String integral, String creditscore) {
        this.integral = integral;
        this.creditscore = creditscore;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getCreditscore() {
        return creditscore;
    }

    public void setCreditscore(String creditscore) {
        this.creditscore = creditscore;
    }
}

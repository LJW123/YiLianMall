package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/23 0023.
 */

public class RedPacketIndexEntity extends HttpResultBean {

    @SerializedName("data")
    public DataBean data;

    public class DataBean {

        @SerializedName("userNum")
        public String userNum;
        @SerializedName("merchantNum")
        public String merchantNum;
        @SerializedName("integralNumberArr")
        public ArrayList<ScoreExponent> integralNumberArr;

    }
}

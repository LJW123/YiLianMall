package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * Created by Ray_L_Pain on 2017/11/24 0024.
 */

public class RedPacketOneKeyRemoveEntity extends HttpResultBean {

    @SerializedName("packet_amount")
    public String packetAmount;

    @SerializedName("packet_num")
    public String packetNum;

    @SerializedName("server_time")
    public String serverTime;

}

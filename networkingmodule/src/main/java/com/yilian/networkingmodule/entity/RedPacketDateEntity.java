package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * Created by Ray_L_Pain on 2017/11/23 0023.
 */

public class RedPacketDateEntity extends HttpResultBean {

    @SerializedName("month_str")
    public String monthStr;
    @SerializedName("claimedNum")
    public String claimedNum;
    @SerializedName("unClaimedNum")
    public int unClaimedNum;
    @SerializedName("unClaimedAmount")
    public String unClaimedAmount;
    @SerializedName("unActivateNum")
    public int unActivateNum;
    @SerializedName("unActivateAmount")
    public String unActivateAmount;
    @SerializedName("userIntegral")
    public String userIntegral;
    @SerializedName("listArr")
    public ArrayList<ListArrBean> listArr;

    public static class ListArrBean {

        public ListArrBean(String packetTime, String packetExist) {
            this.packetTime = packetTime;
            this.packetExist = packetExist;
        }

        @SerializedName("packet_time")
        public String packetTime;

        @SerializedName("packet_exist") //奖励是否存在  0不存在  1存在
        public String packetExist;

        @SerializedName("packet_info")
        public PacketInfo packetInfo;

        public class PacketInfo {
            @SerializedName("id")//奖励id
            public String id;
            @SerializedName("consumer_id")
            public String consumerId;
            @SerializedName("deduct_integral")//消耗奖券数
            public String deductIntegral;
            @SerializedName("amount")//奖励金额  分
            public String amount;
            @SerializedName("created_at")//奖励日期 （生成时间）
            public String createdAt;
            @SerializedName("apply_at")//如果》0  领取时间
            public String applyAt;
            @SerializedName("lost_at")//过期时间
            public String lostAt;
            @SerializedName("status")//0未领取 1已领取 2已过期  3激活未领取
            public String status;
        }

        @Override
        public String toString() {
            return "ListArrBean{" +
                    "packetTime='" + packetTime + '\'' +
                    ", packetExist='" + packetExist;
        }
    }

}

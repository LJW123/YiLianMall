package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * 个人中心icons实体类
 *
 * @author Ray_L_Pain
 * @date 2017/12/4 0004
 */

public class MineIconsEntity extends HttpResultBean {


    @SerializedName("login_status")
    public String loginStatus;

    @SerializedName("user_info")
    public UserInfoBean userInfo;

    public class UserInfoBean {
        @SerializedName("quan")
        public String daiGouQuan;
        //        语音播报开关
        @SerializedName("voice_butt")
        public String voiceButtonStatus;
        @SerializedName("member_name")
        public String memberName;
        @SerializedName("member_photo")
        public String member_photo;
        @SerializedName("integral")
        public String integral;
        @SerializedName("lebi")
        public String lebi;
        @SerializedName("user_bean")
        public String userBean;
        @SerializedName("bean")
        public String bean;
        @SerializedName("user_integral")
        public String userIntegral;
        @SerializedName("user_lebi")
        public String userLebi;
        @SerializedName("lev")
        public String lev;
        //  0未认证  1已认证   提现时新加字段 2018-1-11
        @SerializedName("is_cert")
        public String isCert;
    }

    @SerializedName("agent_info")
    public AgentInfo agentInfo;

    public class AgentInfo {
        @SerializedName("service_status")
        public String server_status;
        @SerializedName("agentRegion")
        public String agentRegion;
        @SerializedName("agent_url")
        public String agent_url;
        @SerializedName("agent_id")
        public String agent_id;
    }

    @SerializedName("merchant_info")
    public MerchantInfoBean merchantInfo;

    public class MerchantInfoBean {
        @SerializedName("merchant_id")
        public String merchantId;
        @SerializedName("mer_status")
        public String mer_status;
        @SerializedName("refuse")
        public String refuse;
    }

    @SerializedName("looked_all")
    public String lookedAll;


    @SerializedName("gameCenter")
    public Bean gameCenter;
    @SerializedName("merchantCenter")
    public Bean merchantCenter;

    public class Bean {
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
        @SerializedName("Subtitle")
        public String Subtitle;
        @SerializedName("type")
        public String type;
        @SerializedName("content")
        public String content;
    }

    @SerializedName("mallOrder")
    public ArrayList<MallBeanEntity> mallOrder;

    @SerializedName("beLooked")
    public ArrayList<IconsBean> beLooked;

    public class IconsBean {
        @SerializedName("title")
        public String title;
        @SerializedName("img")
        public String img;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
    }

    @SerializedName("person_icon")
    public ArrayList<MallBeanEntity> personIcon;

}

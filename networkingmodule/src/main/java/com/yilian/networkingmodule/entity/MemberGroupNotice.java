package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

import java.util.ArrayList;

/**
 * @author Created by  on 2017/12/7.
 */

public class MemberGroupNotice extends HttpResultBean {

    /**
     * code :
     * "type_msg":"",//现金奖励 压话题 幸运购 点点金
     * "type": "",//1 现金奖励 2压话题 3 幸运购 4点点金
     * "relation": "间接会员",//会员关系
     * "user_name": "暂无昵称",//用户名
     * "prize_name": "20.00奖券",//获得奖品
     */
    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        @SerializedName("type_msg")
        public String typeMsg;
        @SerializedName("type")
        public String type;
        @SerializedName("relation")
        public String relation;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("prize_name")
        public String prizeName;
    }
}

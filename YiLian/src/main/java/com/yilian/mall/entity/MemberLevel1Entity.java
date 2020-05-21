package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/29.
 */
public class MemberLevel1Entity extends BaseEntity {

    @SerializedName("member_lev")
    public int memberLev;
    @SerializedName("member_count")
    public String memberCount;
    @SerializedName("member_income")
    public String memberIncome;

    @SerializedName("member_recharge")//会员充值金额
    public String memberRecharge;
    @SerializedName("referee_id")
    public String refereeId;//推荐人id
    @SerializedName("referee_name")//推荐人名字
    public String refereeName;

    @SerializedName("members")
    public ArrayList<Member> membersList;

    public class Member {
        public Member(String memberName,String nameInitial) {
            this.memberName = memberName;
            this.nameInitial = nameInitial;
        }

        @SerializedName("member_id")//会员id
        public String memberId;
        @SerializedName("member_icon")//会员头像
        public String memberIcon;
        @SerializedName("member_name")//会员名字
        public String memberName;
        @SerializedName("reg_time")//注册时间
        public long regTime;
        @SerializedName("name_initial")
        public String nameInitial;
    }
}

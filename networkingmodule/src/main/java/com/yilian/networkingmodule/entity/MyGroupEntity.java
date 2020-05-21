package com.yilian.networkingmodule.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by liuyuqi on 2017/6/19 0019.
 */
public class MyGroupEntity extends BaseEntity{

    /**
     * code :
     * member_lev :
     * member_name :
     * member_photo :
     * member_count :
     * member_income :
     * referee_id :
     * referee_name :
     * members : [{"member_id":"","member_icon":"","member_name":"","reg_time":""},{}]
     */

    @SerializedName("member_lev")
    public String memberLev;
    @SerializedName("member_name")
    public String memberName;
    @SerializedName("member_photo")
    public String memberPhoto;
    @SerializedName("member_count")
    public String memberCount;
    @SerializedName("member_income")
    public String memberIncome;
    @SerializedName("member_cash")
    public String memberCash;
    @SerializedName("referee_id")
    public String refereeId;
    @SerializedName("referee_name")
    public String refereeName;
    @SerializedName("parent_user_id")
    public String parentUserId;
    @SerializedName("members")
    public ArrayList<MembersEntity> memberList;

//    public static class MembersBean {
//
//        /**
//         * browse_new_time : 0
//         * cash : 3045
//         * city : 0
//         * district : 0
//         * info_version : 0
//         * integral : 8955
//         * last_time : 1498467658
//         * member_icon :
//         * member_id : 6264196920909200
//         * member_name :
//         * merchant_fee_deadline : 0
//         * name :
//         * name_initial :
//         * pay_password : 0
//         * phone : 18336620513
//         * photo :
//         * province : 0
//         * rank : 1
//         * referrer : 6264009285700714
//         * reg_device : 6
//         * reg_time : 1498443990
//         * regtime : 1498443990
//         * sex : 0
//         * status : 1
//         * total_award : 0
//         * total_income : 0
//         * total_recharge : 0
//         * user_id : 6264196920909200
//         * virtual_cash : 0
//         * virtual_integral : 0
//         * wechat_openid :
//         * wechat_unionID :
//         */
//
//        @SerializedName("browse_new_time")
//        public String browseNewTime;
//        @SerializedName("cash")
//        public String cash;
//        @SerializedName("city")
//        public String city;
//        @SerializedName("district")
//        public String district;
//        @SerializedName("info_version")
//        public String infoVersion;
//        @SerializedName("integral")
//        public String integral;
//        @SerializedName("last_time")
//        public String lastTime;
//        @SerializedName("member_icon")
//        public String memberIcon;
//        @SerializedName("member_id")
//        public String memberId;
//        @SerializedName("member_name")
//        public String memberName;
//        @SerializedName("merchant_fee_deadline")
//        public String merchantFeeDeadline;
//        @SerializedName("name")
//        public String name;
//        @SerializedName("name_initial")
//        public String nameInitial;
//        @SerializedName("pay_password")
//        public String payPassword;
//        @SerializedName("phone")
//        public String phone;
//        @SerializedName("photo")
//        public String photo;
//        @SerializedName("province")
//        public String province;
//        @SerializedName("rank")
//        public String rank;
//        @SerializedName("referrer")
//        public String referrer;
//        @SerializedName("reg_device")
//        public String regDevice;
//        @SerializedName("reg_time")
//        public String regTime;
//        @SerializedName("regtime")
//        public String regtime;
//        @SerializedName("sex")
//        public String sex;
//        @SerializedName("status")
//        public String status;
//        @SerializedName("total_award")
//        public String totalAward;
//        @SerializedName("total_income")
//        public String totalIncome;
//        @SerializedName("total_recharge")
//        public String totalRecharge;
//        @SerializedName("user_id")
//        public String userId;
//        @SerializedName("virtual_cash")
//        public String virtualCash;
//        @SerializedName("virtual_integral")
//        public String virtualIntegral;
//        @SerializedName("wechat_openid")
//        public String wechatOpenid;
//        @SerializedName("wechat_unionID")
//        public String wechatUnionID;
//    }
}

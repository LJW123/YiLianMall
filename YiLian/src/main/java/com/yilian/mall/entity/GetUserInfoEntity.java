package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class GetUserInfoEntity extends BaseEntity {


    @SerializedName("user_info")
    public UserInfo userInfo;
    @SerializedName("card_url")
    public String cardUrl;
    @SerializedName("url")
    public String url;

    public class UserInfo {
        /**
         * 开店申请id
         */
        @SerializedName("merchant_apply_id")
        public String merchantApplyId;
        /**
         * 审核状态 1表示已付费待审核，2表示审核通过，3表示审核拒绝
         */
        @SerializedName("check_status")
        public String checkStatus;
        /**
         * 审核拒绝原因
         */
        @SerializedName("refund_reason")
        public String refundReason;
        /**
         * 实名认证是否添加认证 1 代表已添加 0代表未添加
         */
        @SerializedName("is_cert")
        public String isCert;
        /**
         * 个性签名
         */
        @SerializedName("personal_signature")
        public String stateOfMind;
        @SerializedName("jwt")
        public String jwt;//登录第三方使用字段
        @SerializedName("jwt_expires_in")
        public String jwtExpiresIn;//jwt字段有效期时长
        @SerializedName("user_id")
        public String userId;
        public String flg; //
        public String reg_device; // 注册设备
        public String regtime;
        public String info_version;
        public String wechat_openid;
        public String phone;//用户手机号
        public String name;//用户昵称
        public String sex;//用户性别(0未知，1男，2女)
        public String integral;//用户当前乐分数量
        public String birthday;//用户生日时间戳（单位：秒）
        public String province;//用户所在省份
        public String city;//用户所在城市
        public String district;//用户所在区、县

        @SerializedName("province_name")
        public String provinceName;//用户所在省份
        @SerializedName("city_name")
        public String cityName;//用户所在城市
        @SerializedName("county_name")
        public String districtName;//用户所在区、县

        public String address;//用户详细地址
        public String intro;//用户个性签名
        public String photo;//用户头像url
        @SerializedName("card_index")
        public String cardIndex;//身份证资料编号
        @SerializedName("card_name")
        public String cardName;//用户真实姓名（未通过审核时为空值）
        @SerializedName("pay_password")
        public boolean payPassword;//用户是否已经设置支付密码
        @SerializedName("login_password")
        public boolean loginPassword;//用户是否已经设置登录密码，可以用来确认用户是否是快捷登录
        public String lebi;//乐币奖励
        public String coupon;//乐券奖励
        @SerializedName("superior_name")
        public String superiorName;//推荐人昵称
        @SerializedName("referrer_phone")
        public String superiorPhone;//推荐人手机号码（如果没有推荐人，则该字段为"0"）
        @SerializedName("voucher")
        public String voucher;
        @SerializedName("voucher_auth") //零购券权限是否开启
        public String voucherAuth;
        @SerializedName("wechat")
        public String weChatIsBind;//0表示未绑定  1表示已绑定
        @SerializedName("zhifubao")
        public String zhiFuBaoIsBind;//0表示未绑定  1表示已绑定
        @SerializedName("hasNews")
        public String hasNews;//是否有用户未读消息 0没有  1有

        @Override
        public String toString() {
            return "UserInfo{" +
                    "flg='" + flg + '\'' +
                    ", reg_device='" + reg_device + '\'' +
                    ", regtime='" + regtime + '\'' +
                    ", info_version='" + info_version + '\'' +
                    ", wechat_openid='" + wechat_openid + '\'' +
                    ", phone='" + phone + '\'' +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", integral='" + integral + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", provinceName='" + provinceName + '\'' +
                    ", cityName='" + cityName + '\'' +
                    ", districtName='" + districtName + '\'' +
                    ", address='" + address + '\'' +
                    ", intro='" + intro + '\'' +
                    ", photo='" + photo + '\'' +
                    ", cardIndex='" + cardIndex + '\'' +
                    ", cardName='" + cardName + '\'' +
                    ", payPassword=" + payPassword +
                    ", loginPassword=" + loginPassword +
                    ", lebi='" + lebi + '\'' +
                    ", coupon='" + coupon + '\'' +
                    ", superiorName='" + superiorName + '\'' +
                    ", superiorPhone='" + superiorPhone + '\'' +
                    ", voucher='" + voucher + '\'' +
                    ", voucherAuth='" + voucherAuth + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetUserInfoEntity{" +
                "userInfo=" + userInfo.toString() +
                ", cardUrl='" + cardUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

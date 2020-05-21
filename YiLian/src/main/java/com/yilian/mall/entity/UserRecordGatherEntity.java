package com.yilian.mall.entity;

import com.google.gson.annotations.SerializedName;

public class UserRecordGatherEntity extends BaseEntity {
    //    为0的情况下 跳转APP内部打开网页 1的情况下 跳转到外部浏览器打开
    @SerializedName("yi_dou_jump")
    public String yiDouJump;
    @SerializedName("yi_dou_bao")
    public String yiDouBao;//0不显示  1显示
    @SerializedName("yi_dou_url")
    public String yiDouBaoUrl;//易豆宝URL
    @SerializedName("agent_apply")
    public String agentApply;////1代表已申请未通过 0未申请   通过状态根据会员等级判断 不跟根据字段判断
    @SerializedName("agent_region")
    public String agentRegion;//申请服务中心所在区域
    /**
     * 会员等级
     */
    @SerializedName("lev")
    public String lev;//会员等级   0普通会员,1VIP会员,2个体商家,3实体商家,4市服务中心,5省服务中心
    @SerializedName("lev_name")
    public String levName;//会员等级名称 //级别名称    普通会员   VIP会员    商家    服务中心
    @SerializedName("agent_url")
    public String agentUrl;//服务中心web页面
    @SerializedName("merchant_id")
    public String merchantId;//商家id
    @SerializedName("agent_id")
    public String agentId;//服务中心的id
    @SerializedName("required_cash")
    public String requiredCash;//(还需要多少钱升级为VIP)
    @SerializedName("qy_url")
    public String qyUrl;//权益URL
    @SerializedName("mer_status")
    public String merStatus;// 0 未成为商家（未缴纳年费） 1 已缴年费成为商家，但未提交资料  2已提交资料待审核 3提交资料审核通过 4审核拒绝
    @SerializedName("refuse")
    public String refuse;//商家审核未通过拒绝原因

    @SerializedName("integral")
    public String integral;//奖券
    /**
     * /用户当前乐发奖励包
     */
    public String balance;

    /**
     * 乐币奖励
     */
    public String lebi;

    /**
     * 可用乐币奖励
     */
    @SerializedName("available_lebi")
    public String availableLebi;

    /**
     * 乐券奖励
     */
    public String coupon;

    /**
     * 中奖凭证数量
     */
    @SerializedName("award_voucher_count")
    public String awardVoucherCount;

    /**
     * 商品收藏数量
     */
    @SerializedName("goods_collect_count")
    public String goodsCollectCount;

    /**
     * 购物车数量
     */
    @SerializedName("cart_count")
    public String cartCount;

    /**
     * 乐分宝领奖励
     */
    @SerializedName("lefenbao")
    public String lefenbao;

    /**
     * 待付款
     */
    @SerializedName("pay_count")
    public String payCount;
    /**
     * 待评论
     */
    @SerializedName("comment_count")
    public String commentCount;
    /**
     * 待收货
     */
    @SerializedName("receipt_count")
    public String receiptCount;
    /**
     * 售后数量
     */
    @SerializedName("service_count")
    public String serviceCount;

    /**
     * 美团-待付款
     */
    @SerializedName("package_pay_count")
    public String packagePayCount;
    /**
     * 美团-待收货
     */
    @SerializedName("package_unuse_count")
    public String packageUnuseCount;
    /**
     * 美团-待评论
     */
    @SerializedName("package_comment_count")
    public String packageCommentCount;
    /**
     * 美团-售后数量
     */
    @SerializedName("package_service_count")
    public String packageServiceCount;

    public String name;

    public String phone;

    public String photo;

    /**
     * 领奖励
     */
    public float income;


    /**
     * 注册时间
     */
    @SerializedName("regtime")
    public String regTime;


    //乐友数量
    @SerializedName("member_count")
    public String memberCount;

    @SerializedName("voucher")
    public String voucher; //零购券
    @SerializedName("voucher_auth")
    public String voucherAuth; //零购券权限是否开启
    @SerializedName("hasNews")
    public String hasNews;//用户是否有未读消息 0没有， 1 有

    /**
     * 乐淘币奖励
     */
    @SerializedName("ltb_amount")
    public String ltbAmount;
    /**
     * 乐淘币图片地址
     */
    @SerializedName("ltb_icon")
    public String ltbIcon;
    /**
     * 乐淘币中间的文字
     */
    @SerializedName("ltb_desc")
    public String ltbDesc;
    /**
     * 乐淘币跳转的url
     */
    @SerializedName("ltb_url")
    public String ltbUrl;
    /**
     * 乐淘币通用跳转-type
     */
    @SerializedName("ltb_type")
    public String ltbType;
    /**
     * 乐淘币通用跳转-content
     */
    @SerializedName("ltb_small_type")
    public String ltbSmallType;

    /**
     * 游戏中心
     */
    @SerializedName("left_name")
    public String leftName;
    /**
     * 游戏中心icon
     */
    @SerializedName("left_icon")
    public String leftIcon;
    /**
     * 游戏中心描述
     */
    @SerializedName("left_desc")
    public String leftDesc;
    /**
     * 游戏中心通用跳转-type
     */
    @SerializedName("left_type")
    public String leftType;
    /**
     * 游戏中心通用跳转-content
     */
    @SerializedName("left_small_type")
    public String leftSmallType;

}

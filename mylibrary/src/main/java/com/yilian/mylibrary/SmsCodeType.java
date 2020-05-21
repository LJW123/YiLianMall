package com.yilian.mylibrary;

/**
 * @author Created by  on 2018/1/18.
 *         发送短信类型
 *         2修改绑定手机号 3重置支付密码 4找回登录密码 5提现申请 6绑定银行卡
 *         7设置支付密码 8绑定手机号 9设置登录密码 10设置商户绑定微信 11审核打款成功
 *         12绑定支付宝账号 13短信注册 14商家资料已审核拒绝 15商家资料已审核通过
 */

public class SmsCodeType {
    /**
     * 快捷登录
     */
    public static final int LOIN_BY_QUICK = 1;
    /**
     * 修改绑定手机号
     */
    public static final int CHANGE_BIND_PHONE = 2;
    /**
     * 重置支付密码
     */
    public static final int RESET_PAY_PASSWORD = 3;
    /**
     * 找回登录密码
     */
    public static final int FIND_LOGIN_PASSWORD = 4;
    /**
     * 提现申请
     */
    public static final int APPLY_WITHDRAW = 5;
    /**
     * 绑定银行卡
     */
    public static final int BIND_BANK_CARD = 6;
    /**
     * 设置支付密码
     */
    public static final int SET_PAY_PASSWORD = 7;
    /**
     * 绑定手机号
     */
    public static final int BIND_PHONE = 8;
    /**
     * 设置登录密码
     */
    public static final int RESET_LOGIN_PASSWORD = 9;
    /**
     * 设置商户绑定微信
     */
    public static final int SET_WECHAT = 10;
    /**
     * 审核打款成功
     */
    public static final int REMIT_SUCCESS = 11;
    /**
     * 绑定支付宝账号
     */
    public static final int SET_ALI_PAY = 12;
    /**
     * 短信注册
     */
    public static final int MESSAGE_REGISTER = 13;
    /**
     * 商家资料已审核拒绝
     */
    public static final int MERCHANT_CHECK_REFUSE = 14;
    /**
     * 商家资料已审核通过
     */
    public static final int MERCHANT_CHECK_PASS = 15;

}

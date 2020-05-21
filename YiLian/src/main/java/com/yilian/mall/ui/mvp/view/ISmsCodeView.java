package com.yilian.mall.ui.mvp.view;

/**
 * @author Created by  on 2018/1/18.
 */

public interface ISmsCodeView extends IBaseView {
    boolean smsCodeDialogIsShowing();
    String getPhone();

    void setVerifyType(int verifyType);

    int getVerifyType();

    int getSmsType();

    String getVoice();

    String getSmsCode();

    /**
     * 获取图形验证码
     *
     * @return
     */
    String getVerifyCode();

    /**
     * 设置发送按钮倒计时
     */
    void startCountDown();

    /**
     * 弹出短信验证码输入弹窗
     */
    void showSmsInputDialog();

    /**
     * 弹出图形验证码
     */
    void showImgCodeDialog();

    /**
     * 刷新图形验证码
     */
    void refreshImgCode();

}

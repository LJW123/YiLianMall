package com.yilian.mall.ui.mvp.view;

import java.io.Serializable;

/**
 * @author Created by  on 2018/1/17.
 */

public interface ICertificationView extends IBaseView, Serializable {
    /**
     * 显示银行名称view
     */
//    void showBankNameView();

//    void hideBankNameView();

    void startSmsCodeActivity();

    void startBindSuccessActivity();

    void startSetPasswordActivity();

//    String getCardNumber();

    String getUserName();

//    String getBankName();

    String getPhone();

    String getIdCard();

    String getServicePhone();

    String getSmsCode();

//    void setBankName(String bankName);

    void setServicePhone(String servicePhone);

    /**
     * 显示输入身份信息View模块
     */
    void showNameView();

    /**
     * 显示输入银行卡信息View模块
     *
     * @param isShowBankName 是否显示银行名称view
     */
//    void showBankView(boolean isShowBankName);

    /**
     * 设置银行卡名称是否可编辑
     *
     * @return
     */
//    void setBankNameFocusable(boolean focusable);

}

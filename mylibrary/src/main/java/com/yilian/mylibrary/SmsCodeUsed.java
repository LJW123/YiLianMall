package com.yilian.mylibrary;

/**
 * @author Created by  on 2018/1/19.
 *         获得短信验证码后 使用验证码的逻辑
 */

public interface SmsCodeUsed {
    /**
     * 使用短信验证码进行的操作
     */
    void smscodeUsed(String smsCode);
}

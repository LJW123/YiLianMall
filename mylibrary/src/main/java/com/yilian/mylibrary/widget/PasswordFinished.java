package com.yilian.mylibrary.widget;

/**
 * Created by  on 2017/6/16 0016.
 */

public interface PasswordFinished{
    /**
     * 密码输入完毕后的回调
     * @param psw 加密后的密码
     */
    void passwordFinished(String psw);
}

package com.yilian.mall.ui.mvp.model;

import android.content.Context;

import rx.Observer;
import rx.Subscription;

/**
 * @author Created by  on 2018/1/18.
 */

public interface ISmsCodeModel extends IBaseModel {
    /**
     * 获取短信
     *
     * @param context
     * @param observer
     * @return
     */
    Subscription getSmsCode(Context context, String phone, int verifyType, int type,
                            String voice, String verifyCode, Observer observer);

    /**
     * 是否显示图形验证码
     *
     * @param context
     * @param observer
     * @return
     */
    Subscription isShowImgCode(Context context, Observer observer);

    /**
     * 检验 验证码
     */
    Subscription checkSmsCode(Context context, String phone, String smsCode, Observer observer);
}

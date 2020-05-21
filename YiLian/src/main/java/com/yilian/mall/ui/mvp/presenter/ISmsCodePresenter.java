package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/18.
 */

public interface ISmsCodePresenter extends IBasePresenter {
    /**
     * 获取短信
     *
     * @param context
     * @return
     */
    Subscription getSmsCode(Context context);

    /**
     * 是否显示图形验证码
     *
     * @param context
     * @return
     */
    Subscription isShowImgCode(Context context);

    /**
     * 检验 验证码
     *
     * @param context
     * @return
     */
    Subscription checkSmsCode(Context context);
}

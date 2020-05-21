package com.yilian.mall.ui.mvp.presenter;

import android.content.Context;

import rx.Subscription;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IEditUserNamePresenter extends IBasePresenter {
    /**
     * 保存数据
     *
     * @param editType 保存数据的类型 0 昵称 1 个性签名
     */
    Subscription saveData(Context context, int editType);
}

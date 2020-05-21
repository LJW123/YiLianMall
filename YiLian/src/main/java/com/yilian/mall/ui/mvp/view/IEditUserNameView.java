package com.yilian.mall.ui.mvp.view;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IEditUserNameView extends IBaseView {
    /**
     * 获取用户填写用户名
     */
    String getUserName();

    /**
     * 获取用户填写心情
     */
    String getStateOfMind();

    void finish();
}

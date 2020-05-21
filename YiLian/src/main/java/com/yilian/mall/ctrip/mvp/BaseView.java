package com.yilian.mall.ctrip.mvp;

/**
 * @author xiaofo on 2018/8/31.
 */

public interface BaseView {
    /**
     * 弹窗
     * @param canceled 点击屏幕其他区域能否取消  false不能  true 能
     */
    void startMyDialog(boolean canceled);
    void stopMyDialog();
    void showToast(String msg);
}

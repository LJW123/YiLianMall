package com.yilian.mall.ui.mvp.view;

import android.content.DialogInterface;
import android.support.annotation.Nullable;

/**
 * @author Created by  on 2018/1/15.
 */

public interface IBaseView {
    void startMyDialog();
    void startMyDialog(boolean isCancel);

    void stopMyDialog();

    void showToast(String msg);

    void showToast(int strId);

    void showSystemV7Dialog(@Nullable String title, @Nullable String message, @Nullable String positiveText
            , @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onClickListener);

}

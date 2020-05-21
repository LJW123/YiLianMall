package com.yilian.mylibrary.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘 控制工具类
 *
 * @author Zg 2018.06.22
 */
public class KeyBordUtils {

    /**
     * 打开软键盘
     *
     * @param mEditText
     * @param mContext
     */
    public static void openKeyBord(final EditText mEditText, final Context mContext) {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
                }
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 200);

    }

    /**
     * 无EditTextView也打开软件盘
     *
     * @param view
     * @param mContext
     */
    public static void openKeyBord(final View view, final Context mContext) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
                }
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 200);

    }

    /**
     * 关闭软键盘
     *
     * @param mContext 上下文
     */
    public static void closeKeyBord(Context mContext) {
        if (mContext != null) {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void showKeyboard(Context mContext, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null == imm)
            return;
        if (isShow) {
            if (((Activity) mContext).getCurrentFocus() != null) {
                //有焦点打开
                imm.showSoftInput(((Activity) mContext).getCurrentFocus(), 0);
            } else {
                //无焦点打开
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            if (((Activity) mContext).getCurrentFocus() != null) {
                //有焦点关闭
                imm.hideSoftInputFromWindow(((Activity) mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                //无焦点关闭
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 判断当前软键盘是否打开
     *
     * @param mContext
     * @return
     */
    public static boolean isSoftInputShow(Context mContext) {

        // 虚拟键盘隐藏 判断view是否为空
        View view = ((Activity) mContext).getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
//       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);

            if (inputmanger != null) {
                return inputmanger.isActive() && ((Activity) mContext).getWindow().getCurrentFocus() != null;
            }
        }
        return false;
    }


}

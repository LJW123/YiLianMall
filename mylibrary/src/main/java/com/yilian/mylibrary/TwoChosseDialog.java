package com.yilian.mylibrary;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * @author Created by  on 2018/2/8.
 */

public class TwoChosseDialog {

    public static PopupWindow getDialog(Activity mContext, String firstText, String secondText) {
        PopupWindow popupWindow = null;
        View v = mContext.getLayoutInflater().inflate(R.layout.library_module_popupwindow_choosebankcard, null, false);
        ((Button) v.findViewById(R.id.btn_first)).setText(firstText);
        ((Button) v.findViewById(R.id.btn_second)).setText(secondText);
        popupWindow = new PopupWindow(v, -1, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.library_module_AnimationFade);
        // 点击其他地方消失
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }
}

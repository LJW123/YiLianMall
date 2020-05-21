package com.yilian;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.yilian.mylibrary.R;
import com.yilian.mylibrary.ScreenUtils;

/**
 * @author xiaofo on 2018/7/3.
 */

public class BaseDialogFragment extends AppCompatDialogFragment {
    /**
     * 弹出V7版本系统弹窗
     *
     * @param title
     * @param message
     * @param positiveText
     * @param negativeText
     * @param onClickListener
     */
    public void showSystemV7Dialog(@Nullable String title, @Nullable CharSequence message, @Nullable String positiveText, @Nullable String negativeText,
                                   @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener)
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.library_module_color_333));
    }
    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int strId) {
        Toast.makeText(getContext(), strId, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.library_module_BottomToTopAnim);
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = (int) (ScreenUtils.getScreenHeight(getContext()) / 1.3);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }




}

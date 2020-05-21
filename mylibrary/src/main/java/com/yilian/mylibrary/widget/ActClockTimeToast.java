package com.yilian.mylibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yilian.mylibrary.R;
import com.yilian.mylibrary.ScreenUtils;

/**
 * @author Created by  on 2017/11/22 0022.
 */

public class ActClockTimeToast {
    public static Toast getToast(Context context, String msg, int duration) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.library_module_toast_clock_time, null);
        TextView tvTime = (TextView) inflate.findViewById(R.id.tv_time);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvTime.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(context) - 300;
//        layoutParams.setMargins(DPXUnitUtil.dp2px(context,15),0,DPXUnitUtil.dp2px(context,15),0);
        tvTime.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(inflate);
        return toast;
    }
}

package com.leshan.ylyj.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leshan.ylyj.testfor.R;


/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ToastMiddleUtils {

    private static Toast toast;

    public static void ToastMiddle(Context context, String data) {
        Toast toast = Toast.makeText(context, data, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * dialog带图片
     */
    public static void ToastShow(Context context, ViewGroup root, String str) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_img, root);
        TextView text = (TextView) view.findViewById(R.id.text_toast);
        text.setText(str); // 设置显示文字
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0); // Toast显示的位置
        toast.setDuration(Toast.LENGTH_SHORT); // Toast显示的时间
        toast.setView(view);
        toast.show();
    }

}

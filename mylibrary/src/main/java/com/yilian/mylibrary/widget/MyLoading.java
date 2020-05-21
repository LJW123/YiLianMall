package com.yilian.mylibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yilian.mylibrary.R;


public class MyLoading extends Dialog {

    private Context context = null;

    public MyLoading(Context context) {
        super(context);
        this.context = context;
    }

    public MyLoading(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param
     * @return
     */
    public static MyLoading createLoadingDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.library_module_loading, null);// 得到加载view
        FrameLayout dialogView = (FrameLayout) view.findViewById(R.id.dialog_view);// 加载布局
        ImageView loading = (ImageView) view.findViewById(R.id.loading);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.library_module_loading_animation);// 加载动画
        loading.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画

        MyLoading loadingDialog = new MyLoading(context, R.style.library_module_loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(dialogView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

        return loadingDialog;
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param isCancle 点击屏幕其他地方 dialog是否消失
     * @return
     */
    public static MyLoading createLoadingDialog(Context context, boolean isCancle) {

        View view = LayoutInflater.from(context).inflate(R.layout.library_module_loading, null);// 得到加载view
        FrameLayout dialogView = (FrameLayout) view.findViewById(R.id.dialog_view);// 加载布局
        ImageView loading = (ImageView) view.findViewById(R.id.loading);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.library_module_loading_animation);// 加载动画
        loading.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画

        MyLoading loadingDialog = new MyLoading(context, R.style.library_module_loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCanceledOnTouchOutside(isCancle);//点击屏幕其他地方 dialog是否消失
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(dialogView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        return loadingDialog;
    }

}

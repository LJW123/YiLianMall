package com.yilian.mall.shoppingcard.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilian.mall.R;


/**
 * 提醒 去绑卡
 * Created by Zg on 2018/6/05.
 */
public class CardWarnCommonPopwindow extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_left;
    private TextView tv_right;

    public CardWarnCommonPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.card_popupwindow_warn_comm, null);
        this.mContext = mContext;
       /* 初始化view */
        initView();
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void initView() {
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        tv_content = view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.GONE);
        tv_left = view.findViewById(R.id.tv_left);
        tv_right = view.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.GONE);


    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 弹出对话框
     *
     * @param v
     */
    public void showPop(View v) {
        showAtLocation(v, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.3f);
    }

    /**
     * 设置标题
     *
     * @param title 标题内容
     */
    public void setTitle(String title) {
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        tv_content.setVisibility(View.VISIBLE);
        tv_content.setText(content);
    }
    public void setContent(SpannableString spannableString) {
        tv_content.setVisibility(View.VISIBLE);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());//设置可点击状态
        tv_content.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
        tv_content.setText(spannableString);
    }

    /**
     * 设置左按钮 文本及点击事件
     *
     * @param leftStr  文本
     * @param listener 点击事件
     */
    public void setLeft(String leftStr, View.OnClickListener listener) {
        tv_left.setText(leftStr);
        tv_left.setOnClickListener(listener);
    }

    public void setLeftColor(String colorStr){
        tv_left.setTextColor(Color.parseColor(colorStr));
    }

    /**
     * 设置右按钮 文本及点击事件
     *
     * @param rightStr 文本
     * @param listener 点击事件
     */
    public void setRight(String rightStr, View.OnClickListener listener) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(rightStr);
        tv_right.setOnClickListener(listener);
    }

    /**
     * 设置 是否重写物理返回键 关闭当前activity
     */
    public void setCloseActivity() {
        setOutsideTouchable(false);
        //设置可 重写返回键事件
        view.setFocusable(true); // 这个很重要
        setFocusable(false);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    ((Activity) mContext).finish();
                    return true;
                }
                return false;
            }
        });
    }
}
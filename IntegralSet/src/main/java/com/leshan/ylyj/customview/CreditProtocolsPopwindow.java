package com.leshan.ylyj.customview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leshan.ylyj.testfor.R;
import com.vondear.rxtools.RxDeviceTool;
import com.yilian.mylibrary.Constants;


/**
 * Created by Zg on 2017/8/30.
 */
public class CreditProtocolsPopwindow extends PopupWindow {

    private Context mContext;
    private View view;

    private TextView content;//内容
    private TextView remind_tv;
    private TextView disagree, agree;

    public CreditProtocolsPopwindow(final Context mContext, String str) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_credit_protocols, null);
        this.mContext = mContext;

        content = view.findViewById(R.id.content);
        remind_tv = view.findViewById(R.id.remind_tv);
        disagree = view.findViewById(R.id.disagree);
        agree = view.findViewById(R.id.agree);
        if (!TextUtils.isEmpty(str)) {
            Spanned text = Html.fromHtml(str);
            com.orhanobut.logger.Logger.i("协议内容：" + text);
            content.setText(text);
            content.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        SpannableString spannableString = new SpannableString("点击同意即表示您已阅读并同意《益联益家益点信用协议》");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
                intent.putExtra(Constants.SPKEY_URL, Constants.YLYJ_CREDIT_INFO);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#F22424")); //设置颜色
                ds.setUnderlineText(false);
            }
        }, 14, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        remind_tv.setMovementMethod(LinkMovementMethod.getInstance());
        remind_tv.setText(spannableString);

        // 设置外部不可点击
        this.setOutsideTouchable(false);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.view.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = view.findViewById(R.id.pop_layout).getTop();
//
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        DisplayMetrics dm = RxDeviceTool.getDisplayMetrics(mContext);
        this.setHeight((int) (dm.heightPixels * 0.5847));
//        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popup_window_anim);
    }

    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        disagree.setOnClickListener(listener);
    }
    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        agree.setOnClickListener(listener);
    }
}
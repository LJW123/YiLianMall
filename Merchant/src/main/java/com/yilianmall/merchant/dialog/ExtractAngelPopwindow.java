package com.yilianmall.merchant.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilianmall.merchant.R;

import java.math.BigDecimal;


/**
 * 提取乐天使 弹出
 * Created by Zg on 2018/6/05.
 */
public class ExtractAngelPopwindow extends PopupWindow {


    private Context mContext;
    private View view;
    private TextView tv_content;
    private TextView tv_cancel, tv_confirm;


    public ExtractAngelPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.merchant_module_popupwindow_extract_angel, null);
        this.mContext = mContext;
        /* 初始化view */
        initView();
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
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
        tv_content = view.findViewById(R.id.tv_content);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_confirm = view.findViewById(R.id.tv_confirm);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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


    public void setContent(SpannableString spannableString) {
        tv_content.setText(spannableString);
        //必须设置否则无效
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void confirm(View.OnClickListener listener) {
        tv_confirm.setOnClickListener(listener);
    }

}
package com.yilian.mall.ctrip.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilian.mall.R;


/**
 * 订单 取消政策
 * Created by Zg on 2018/9/21.
 */
public class CtripCommitOrderWriteExplainPopwindow extends PopupWindow {
    private Context mContext;
    private View view;



    public CtripCommitOrderWriteExplainPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.ctrip_popupwindow_commit_order_write_explain, null);
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


}
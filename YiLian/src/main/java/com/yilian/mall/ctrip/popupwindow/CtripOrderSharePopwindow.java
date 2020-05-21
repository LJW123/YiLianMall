package com.yilian.mall.ctrip.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilian.mall.R;


/**
 * 订单分享
 * Created by Zg on 2018/10/8.
 */
public class CtripOrderSharePopwindow extends PopupWindow {
    private Context mContext;
    private View view;
    private LinearLayout ll_wechat,ll_friends_circle,ll_qq,ll_qzone;
    private TextView tv_cancel;


    public CtripOrderSharePopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.ctrip_popupwindow_order_share, null);
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
        ll_wechat = view.findViewById(R.id.ll_wechat);
        ll_friends_circle = view.findViewById(R.id.ll_friends_circle);
        ll_qq = view.findViewById(R.id.ll_qq);
        ll_qzone = view.findViewById(R.id.ll_qzone);
        tv_cancel = view.findViewById(R.id.tv_cancel);


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
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.3f);
    }

    /**
     * 微信 分享
     *
     * @param listener 点击事件
     */
    public void setWechat( View.OnClickListener listener) {
        ll_wechat.setOnClickListener(listener);
    }
    /**
     * 微信朋友圈 分享
     *
     * @param listener 点击事件
     */
    public void setFriendsCircle( View.OnClickListener listener) {
        ll_friends_circle.setOnClickListener(listener);
    }
    /**
     * QQ 分享
     *
     * @param listener 点击事件
     */
    public void setQq( View.OnClickListener listener) {
        ll_qq.setOnClickListener(listener);
    }
    /**
     * QQ空间 分享
     *
     * @param listener 点击事件
     */
    public void setQzone( View.OnClickListener listener) {
        ll_qzone.setOnClickListener(listener);
    }

}
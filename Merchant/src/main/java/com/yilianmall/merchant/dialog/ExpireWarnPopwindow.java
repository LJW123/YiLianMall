package com.yilianmall.merchant.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yilian.mylibrary.DateUtils;
import com.yilianmall.merchant.R;


/**
 * 到期提醒 弹出
 * Created by Zg on 2018/8/08.
 */
public class ExpireWarnPopwindow extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView tv_remind_1, tv_remind_2, tv_remind_3, tv_remind_4;
    private TextView tv_renew, tv_cancel;


    public ExpireWarnPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.merchant_popupwindow_expire_warn, null);
        this.mContext = mContext;
        /* 初始化view */
        initView();
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        this.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
        //设置可 重写返回键事件
        view.setFocusable(true); // 这个很重要
        setFocusable(false);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void initView() {
        tv_remind_1 = view.findViewById(R.id.tv_remind_1);
        tv_remind_2 = view.findViewById(R.id.tv_remind_2);
        tv_remind_3 = view.findViewById(R.id.tv_remind_3);
        tv_remind_4 = view.findViewById(R.id.tv_remind_4);
        tv_renew = view.findViewById(R.id.tv_renew);
        tv_cancel = view.findViewById(R.id.tv_cancel);
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


    public void setContent(long last_days, long merchantDueTime) {
        //判断是否已过期
        if (last_days <= 0) {
            //已过期
            tv_remind_1.setText("您好，您的店铺因逾期未缴费，已被暂停服务");
            tv_remind_2.setText(" 逾期时间：");
            tv_remind_3.setText(DateUtils.formatDate2(merchantDueTime * 1000));
            tv_remind_4.setText("请及时续费，恢复店铺的正常使用");
            tv_renew.setText("立即续费");
            tv_cancel.setText("返回");

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
        } else {
            //未过期
            tv_remind_1.setText("您好，您的店铺即将到期");
            tv_remind_2.setText(" 剩余：");
            tv_remind_3.setText(last_days + "天");
            tv_remind_4.setText("请及时续费，以免影响您的正常使用");
            tv_renew.setText("续缴平台运营费");
            tv_cancel.setText("暂不续费");

            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 续费
     */
    public void renew(View.OnClickListener listener) {
        tv_renew.setOnClickListener(listener);
    }

    /**
     * 取消
     */
    public void cancel(View.OnClickListener listener) {
        tv_cancel.setOnClickListener(listener);
    }


}
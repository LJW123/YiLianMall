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

import java.text.SimpleDateFormat;


/**
 * 订单 房型详情
 * Created by Zg on 2018/9/21.
 */
public class CtripOrderCostPopwindow extends PopupWindow {
    private Context mContext;
    private View view;
    private TextView tv_title;

    private TextView dayInclusiveAmount, date, Breakfast, InclusiveAmount;

    public CtripOrderCostPopwindow(final Context mContext) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.ctrip_popupwindow_order_cost, null);
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

        dayInclusiveAmount = view.findViewById(R.id.dayInclusiveAmount);
        date = view.findViewById(R.id.date);
        Breakfast = view.findViewById(R.id.Breakfast);
        InclusiveAmount = view.findViewById(R.id.InclusiveAmount);

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
        tv_title.setText(title);
    }


    public void setContent(String dayInclusiveAmount, long TimeSpanStart, long TimeSpanEnd, int Breakfast, String InclusiveAmount) {
        this.dayInclusiveAmount.setText(dayInclusiveAmount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        this.date.setText(String.format("%s至%s", sdf.format(TimeSpanStart * 1000), sdf.format(TimeSpanEnd * 1000)));
        String breakfast = "无早餐";
        if (Breakfast == 1) {
            breakfast = "单份早餐";
        } else if (Breakfast == 2) {
            breakfast = "双份早餐";
        }
        this.Breakfast.setText(breakfast);
        this.InclusiveAmount.setText(String.format("¥%s", InclusiveAmount));
    }
}
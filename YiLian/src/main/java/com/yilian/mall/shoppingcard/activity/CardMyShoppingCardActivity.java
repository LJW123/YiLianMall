package com.yilian.mall.shoppingcard.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.utils.CheckApkExistUtil;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mylibrary.RxUtil;
import io.reactivex.functions.Consumer;

/**
 * 作者：马铁超 on 2018/11/13 18:32
 * 我的购物卡
 */

public class CardMyShoppingCardActivity extends BaseAppCompatActivity {
    private TextView v3Title;
    private ImageView v3Back, ivOppencard;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcard_home);
        initView();
        initData();
        initListener();
    }





    private void initView() {
        v3Title = findViewById(R.id.v3Title);
        v3Back = findViewById(R.id.v3Back);
        ivOppencard = findViewById(R.id.iv_oppencard);
    }

    private void initData() {
        v3Title.setText("我的购物卡");
    }

    private void initListener() {
        //立即开卡
        RxUtil.clicks(ivOppencard, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                oppenCardDialog();
            }
        });
        //返回
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                onBackPressed();
            }
        });
    }


    private void oppenCardDialog() {
        dialog = new Dialog(mContext, R.style.custom_window_dialog);
        RelativeLayout layout = (RelativeLayout) ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.dailog_gootherapp, null);
        TextView tv_dialog_cancel = layout.findViewById(R.id.tv_dialog_cancel);
        TextView tv_dialog_confirm = layout.findViewById(R.id.tv_dialog_confirm);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCanceledOnTouchOutside(true);
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() * 0.9); // 设置宽度
        lp.height = (int) (display.getHeight() * 0.25); // 设置高度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断是否安装了天使应用
               if(CheckApkExistUtil.isApplicationAvilible(mContext,"letaoangle.com.letaoangle")){
                   String packgeName = "letaoangle.com.letaoangle";
                   String className = "letaoangle.com.letaoangle.phaseone.activity.shoppingcard.RechargeShoppingCardActivity";
                   JumpCardActivityUtils.toOtherAppActivity(CardMyShoppingCardActivity.this,packgeName,className);
               }else{
                 // 跳转下载页面
               }
            }
        });

    }
}

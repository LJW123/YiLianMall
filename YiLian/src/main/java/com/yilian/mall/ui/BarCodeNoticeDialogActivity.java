package com.yilian.mall.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.yilian.mall.BaseDialogActivity;
import com.yilian.mall.R;

/**
 *
 * 条码横屏效果
 * Created by ZYH on 2017/12/17 0017.
 */

public class BarCodeNoticeDialogActivity extends BaseDialogActivity implements View.OnClickListener {
    private LinearLayout linearLayout;
    private TextView tvNoticeHorn;
    private TextView tvKnow;
    private LinearLayout llInnerRotion2;
    private LinearLayout llParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_notice);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        initView();
        initListner();
    }

    private void initListner() {
        tvKnow.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.view.ViewPropertyAnimator animator=linearLayout.animate();
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                WindowManager wm = (WindowManager) BarCodeNoticeDialogActivity.this.getSystemService(Context.WINDOW_SERVICE);
                int height = wm.getDefaultDisplay().getHeight();
                ViewGroup.LayoutParams lp=linearLayout.getLayoutParams();
                lp.width=height;
                super.onAnimationStart(animation);
            }
        });
        linearLayout.animate().rotation(90).setDuration(500);
        llInnerRotion2.animate().rotation(90).setDuration(300);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
    private void initView() {
        llParent= (LinearLayout) findViewById(R.id.ll_parent);
        llInnerRotion2= (LinearLayout) findViewById(R.id.ll_inner_rotion2);
        tvNoticeHorn= (TextView) findViewById(R.id.tv_notice_horn);
        linearLayout= (LinearLayout) findViewById(R.id.ll_inner_ration1);
        tvKnow= (TextView) findViewById(R.id.tv_know);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_know:
                android.view.ViewPropertyAnimator animator=llParent.animate();
                llParent.animate().alpha(0).setDuration(100);
                animator.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        llParent.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.ll_inner_ration1:
                finish();
                break;
        }

    }
}

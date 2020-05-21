package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.RxUtil;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * 用于在fragment上loading不显示报错的activity
 *
 * @author Ray_L_Pain
 * @date 2017/12/6 0006
 */

public class LoadingShopCarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_loading);
        initView();
        initTimer();
    }

    private void initTimer() {
        Subscription subscription = RxUtil.countDown(5)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        开始倒计时
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Logger.i(getClass().getSimpleName() + "aLong:" + aLong);
                        if (aLong == 0) {
                            finish();
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void initView() {
        ImageView loading = (ImageView) findViewById(R.id.loading);

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);// 加载动画
        loading.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画
    }
}

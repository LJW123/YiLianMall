package com.yilian.mylibrary;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Created by  on 2017/12/7.
 */

public class BaseFragmentActivity extends FragmentActivity {
    public Context mContext;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //禁止所有activity横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
    }

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }

    @Override
    protected void onDestroy() {
        GlideUtil.pauseRequests(this.getApplicationContext());
        //取消mActivityStack对该activity的持有，防止内存泄漏
        AppManager.getInstance().killActivity(this);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlideUtil.resumeRequests(this.getApplicationContext());
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, mContext);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, mContext);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}

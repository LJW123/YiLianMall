package com.yilian.luckypurchase.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseFragmentActivity extends AppCompatActivity {

    public SharedPreferences sp;
    public Context context;
    public BitmapDisplayConfig bitmapConfig;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Override
    protected void onDestroy() {
        //取消mActivityStack对该activity的持有，防止内存泄漏
        AppManager.getInstance().killActivity(this);
        GlideUtil.pauseRequests(this.getApplicationContext());

        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //防止当前activity长时间存于后台或者内存不足被系统回收，默认缓存fragment的内容及状态
        // 这时fragment中的getActivity为null的情况，如果有缓存就清除重新加载
        if (null != savedInstanceState) {
            String FRAGMENTS_TAG = "Android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }

        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏

        sp = getSharedPreferences("UserInfor", 0);
        context = this;
        bitmapConfig = new BitmapDisplayConfig();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlideUtil.resumeRequests(this.getApplicationContext());
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, context);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, context);
        MobclickAgent.onPause(this);
    }

    public String getToken() {
        Long token = Long.valueOf(sp.getString("token", "0")) + Long.valueOf(sp.getString("server_salt", "0"));
        return String.valueOf(token);
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return sp.getBoolean(Constants.SPKEY_STATE, false);
    }

    public void onBack(View v) {

        dismissInputMethod();
        finish();
    }

    public void rightTextview(View v) {

    }


    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseFragmentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseFragmentActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseFragmentActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

        Logger.i("隐藏软键盘");
    }

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseFragmentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseFragmentActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(BaseFragmentActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 字体大小不随系统改变而变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    private CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }
}

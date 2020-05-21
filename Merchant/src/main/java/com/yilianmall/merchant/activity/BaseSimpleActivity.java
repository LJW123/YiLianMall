package com.yilianmall.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.widget.MyLoading;
import com.yilianmall.merchant.R;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class BaseSimpleActivity extends Activity {

    public static final int LOCATION_REQUEST_CODE = 99;
    public SharedPreferences sp;
    public Context mContext;
    public BitmapDisplayConfig bitmapConfig;
    public MyLoading myloading;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        sp = getSharedPreferences("UserInfor", 0);
        mContext = this;

        bitmapConfig = new BitmapDisplayConfig();

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

//    public String getToken() {
//        Long token = Long.valueOf(sp.getString("token", "0")) + Long.valueOf(sp.getString("server_salt", "0"));
//        return String.valueOf(token);
//    }

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

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(mContext);
            Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
        }
        if (myloading != null && !isFinishing()) {
            myloading.show();
            Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                    if (myloading != null && myloading.isShowing()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stopMyDialog();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startMyDialog(boolean isCancle) {
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(mContext, isCancle);
        }
        if (myloading != null && !isFinishing()) {
            myloading.show();
            Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            if (!isFinishing()) {
                myloading.dismiss();
                Logger.i(this.getClass().getName() + "  取消了dialog  " + this.toString());
            }
            myloading = null;
        }
    }

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int strId) {
        Toast.makeText(mContext, strId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示框
     *
     * @param title        标题
     * @param msg          内容
     * @param detailMsg    详细内容
     * @param iconId       内容 图标 没有就传0
     * @param gravity      图标和内容位置
     * @param positiveText 确定字样
     * @param negativetext 取消字样
     * @param cancelable   点击屏幕意外或者返回键是否消失
     * @param listener     按键监听
     */
    public void showDialog(@Nullable String title, @Nullable String msg, @Nullable String detailMsg,
                           int iconId, int gravity, @Nullable String positiveText, @Nullable String negativetext,
                           boolean cancelable, OnClickListener listener, Context context
    ) {
        VersionDialog.Builder builder = new VersionDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg, iconId, gravity);
        builder.setDetailMessage(detailMsg);
        builder.setPositiveButton(positiveText, listener);
        builder.setNegativeButton(negativetext, listener);
        builder.setCancelable(cancelable);
        if (!isFinishing()) {
            builder.create().show();
        }
    }

    /**
     * 弹出V7版本系统弹窗
     *
     * @param title
     * @param message
     * @param positiveText
     * @param positiveListener
     * @param negativeText
     * @param negativeListener
     */
    public void showSystemV7Dialog(String title, String message, String positiveText, OnClickListener positiveListener, String negativeText,
                                   OnClickListener negativeListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_333));

    }

    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseSimpleActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseSimpleActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseSimpleActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

        Logger.i("隐藏软键盘");
    }

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseSimpleActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseSimpleActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(BaseSimpleActivity.this.getCurrentFocus().getWindowToken(),
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消mActivityStack对该activity的持有，防止内存泄漏
        AppManager.getInstance().killActivity(this);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

}

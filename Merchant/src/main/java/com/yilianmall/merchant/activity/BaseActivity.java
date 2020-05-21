package com.yilianmall.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.LeFenLoading;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.VersionDialog;
import com.yilian.mylibrary.event.EventDownLoadProgress;
import com.yilian.mylibrary.event.EventGameDownloadStatusChanged;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.event.RemoveMerchantManageOrderList;
import com.yilianmall.merchant.event.UpdateMerchantManageOrderList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity {

    protected BaseActivity mContext;
    protected SharedPreferences sp;
    private InputMethodManager manager;
    public String device_index, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏

//     Activity是继承自AppCompatActivity，所以requestWindowFeature(Window.FEATURE_NO_TITLE);这句失效了。
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EventBus.getDefault().register(this);
        AppManager.getInstance().addActivity(this);
        sp = getSharedPreferences(Constants.SP_FILE, 0);
        mContext = this;
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        device_index = RequestOftenKey.getDeviceIndex(mContext);
        token = RequestOftenKey.getToken(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, mContext);
    }

    private CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

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
                           boolean cancelable, DialogInterface.OnClickListener listener, Context context
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
     * @param negativeText
     * @param onClickListener
     */
    public void showSystemV7Dialog(@Nullable String title, @Nullable String message, @Nullable String positiveText
            , @Nullable String negativeText,
                                   @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener)
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_333));

    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int strId) {
        Toast.makeText(mContext, strId, Toast.LENGTH_SHORT).show();
    }

    public LeFenLoading myloading;

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (myloading == null && null != mContext) {
            myloading = LeFenLoading.createLoadingDialog(mContext);
            Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
        }
        if (myloading != null && !isFinishing()) {
            myloading.show();
            Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
        }
    }

    public void startMyDialog(boolean isCancel) {
        if (myloading == null && null != mContext) {
            myloading = LeFenLoading.createLoadingDialog(mContext, isCancel);
        }
        if (myloading != null && !isFinishing()) {
            myloading.show();
            Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
        }
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return sp.getBoolean(Constants.SPKEY_STATE, false);
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

    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

        Logger.i("隐藏软键盘");
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

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 接收游戏更新事件
     * {@link com.yilian.mall.ui.GameHomePageActivity}
     *
     * @param eventDownLoadProgress
     */
    @Subscribe
    public void onMessageEvent(EventDownLoadProgress eventDownLoadProgress) {

    }

    /**
     * 接收游戏下载状态改变时间
     * {@link com.yilian.mall.ui.GameHomePageActivity}
     *
     * @param eventGameDownloadStatusChanged
     */
    @Subscribe
    public void onMessageGameDownloadStatusChanged(EventGameDownloadStatusChanged eventGameDownloadStatusChanged) {

    }

    /**
     * 接收发货完成信息，刷新页面1
     *
     * @param removeMerchantManageOrderList
     */
    @Subscribe
    public void refreshMerchantManageOrderDetailActivity(RemoveMerchantManageOrderList removeMerchantManageOrderList) {

    }

    /**
     * 接收发货完成信息，刷新页面2 和1同样的性质 只是刷新时机不同（该方法在发货后，订单处于部分发货状态时刷新界面使用）
     *
     * @param updateMerchantManageOrderList
     */
    @Subscribe
    public void updateMerchantManageOrderList(UpdateMerchantManageOrderList updateMerchantManageOrderList) {

    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    public <M> void addNewSubscription(Observable<M> observable, Subscriber<M> subscriber) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }


}

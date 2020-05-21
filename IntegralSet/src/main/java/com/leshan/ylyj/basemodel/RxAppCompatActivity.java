package com.leshan.ylyj.basemodel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.leshan.ylyj.testfor.R;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.umeng.analytics.MobclickAgent;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.BaseEntity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.widget.EToast;
import com.yilian.mylibrary.widget.MyLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;


public class RxAppCompatActivity extends AppCompatActivity {

    protected final BehaviorSubject<ActivityEvent> lifeSubject = BehaviorSubject.create();

    public <T> Observable.Transformer<T, T> bindUntilEvent(final ActivityEvent bindEvent) {
        //被监视的Observable
        final Observable<ActivityEvent> observable = lifeSubject.takeFirst(new Func1<ActivityEvent, Boolean>() {
            @Override
            public Boolean call(ActivityEvent event) {
                return event.equals(bindEvent);
            }
        });

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceOb) {

                return sourceOb.takeUntil(observable);
            }
        };
    }

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        mContext = this;
        EventBus.getDefault().register(this);

        //  lifeSubject.onNext(ActivityEvent.CREATE);
    }

    @Subscribe
    public void notifyItem(BaseEntity item) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        lifeSubject.onNext(ActivityEvent.START);
    }


    @Override
    protected void onStop() {
        super.onStop();
        lifeSubject.onNext(ActivityEvent.STOP);
    }

    private CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeSubject.onNext(ActivityEvent.RESUME);
        GlideUtil.resumeRequests(this.getApplicationContext());
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, mContext);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeSubject.onNext(ActivityEvent.PAUSE);
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, mContext);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    /**
     * 判断是否登录
     */
    public boolean isLogin() {
        return PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, this);
    }

    public void onBack(View v) {

        dismissInputMethod();
        finish();
    }

    /**
     * 是否实名认证
     *
     * @return
     */
    public boolean isCert() {
        String isCert = PreferenceUtils.readStrConfig(Constants.IS_CERT, mContext);
        if ("1".equals(isCert)) {
            return true;
        }
        return false;
    }
    public MyLoading myloading;

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
            Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
        }
    }

    /**
     * @param isCancle 点击屏幕是否能取消 false 不可以取消 true 可以取消
     */
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
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int strId) {
        Toast.makeText(mContext, strId, Toast.LENGTH_SHORT).show();
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
            , @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener)
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(this.getResources().getColor(R.color.library_module_color_333));

    }

    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) RxAppCompatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (RxAppCompatActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(RxAppCompatActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

        Logger.i("隐藏软键盘");
    }

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) RxAppCompatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (RxAppCompatActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(RxAppCompatActivity.this.getCurrentFocus().getWindowToken(),
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
        lifeSubject.onNext(ActivityEvent.DESTROY);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        EventBus.getDefault().unregister(this);
        //取消mActivityStack对该activity的持有，防止内存泄漏
        AppManager.getInstance().killActivity(this);
        EToast.reset();
        GlideUtil.pauseRequests(this.getApplicationContext());

    }
}

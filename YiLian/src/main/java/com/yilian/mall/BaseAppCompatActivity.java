package com.yilian.mall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.amap.api.location.AMapLocation;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.yilian.mall.entity.eventEntity.RefreshActHomePageDataEntity;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.EToast;
import com.yilian.mall.widgets.MyLoading;
import com.yilian.mall.widgets.VersionDialog;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by  on 2017/4/27 0027.
 */

public class BaseAppCompatActivity extends AppCompatActivity {

    public SharedPreferences sp;
    public Context mContext;
    public MyLoading myloading;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        sp = getSharedPreferences("UserInfor", 0);
        mContext = this;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        EventBus.getDefault().unregister(this);
        //取消mActivityStack对该activity的持有，防止内存泄漏
        AppManager.getInstance().killActivity(this);
        EToast.reset();
        GlideUtil.pauseRequests(this.getApplicationContext());
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

    @Subscribe
    public void refreshData(RefreshActHomePageDataEntity refreshActHomePageDataEntity) {
    }

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, mContext);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlideUtil.resumeRequests(this.getApplicationContext());
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, mContext);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
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

    /**
     * 是否有支付密码
     * @return
     */
    public boolean hasPassword() {
        return com.yilian.mall.utils.PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD, mContext, false);
    }

    /**
     * 是否已实名认证
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

    //TODO 是否绑定了银行卡
    public boolean isBindBankCard() {
        return false;
    }

    public void onBack(View v) {

        dismissInputMethod();
        finish();
    }

    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseAppCompatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseAppCompatActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseAppCompatActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

        Logger.i("隐藏软键盘");
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
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            if (!isFinishing()) {
                myloading.dismiss();
                Logger.i(this.getClass().getName() + "  取消了dialog  " + this.toString());
            }
        }
        myloading = null;
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

    public void showToast(String msg) {
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
            , @Nullable String negativeText, @Nullable DialogInterface.OnClickListener onClickListener) {
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

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseAppCompatActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseAppCompatActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(BaseAppCompatActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 弹出软键盘 可处理键盘收起后不能收起的问题
     * @param view
     */
    protected void showInputMethod(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


}
